package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.exceptions.ConflictException;
import com.m42hub.m42hub_api.exceptions.CustomNotFoundException;
import com.m42hub.m42hub_api.exceptions.UnauthorizedException;
import com.m42hub.m42hub_api.project.entity.Member;
import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.project.entity.Project;
import com.m42hub.m42hub_api.project.repository.MemberRepository;
import com.m42hub.m42hub_api.project.repository.ProjectRepository;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final ProjectUnfilledRoleService projectUnfilledRoleService;
    private final MemberStatusService memberStatusService;
    private final ProjectRepository projectRepository;
    private final MemberRepository repository;
    private final UserService userService;

    @Transactional
    public Member save(Member member) {
        return repository.save(member);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Member> findByUsername(String username) {
        User user = userService.findByUsername(username).orElseThrow(() ->
                new CustomNotFoundException("Usuário não encontrado")
        );
        return repository.findAllByUserId(user.getId());
    }

    public List<Member> findByProjectId(UUID projectId) {
        return repository.findAllByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public Map<UUID, List<Member>> findByProjectIds(List<UUID> projectIds) {
        List<Member> members = repository.findByProjectIdIn(projectIds);
        return members.stream().collect(Collectors.groupingBy(Member::getProjectId));
    }

    @Transactional
    public Optional<Member> approve(Long memberId, UUID userId) {
        Member member = repository.findById(memberId).orElseThrow(() ->
                new CustomNotFoundException("Membro não encontrado")
        );

        Project project = projectRepository.findById(member.getProjectId()).map(foundProject -> {
            if (!isUserProjectManager(foundProject.getId(), userId)) {
                throw new UnauthorizedException("Usuário que solicitou alteração não é o idealizador do projeto");
            }
            if (isRoleFilled(foundProject, member.getRoleId())) {
                throw new ConflictException("Cargo já preenchido");
            }
            return foundProject;
        }).orElseThrow(() ->
                new CustomNotFoundException("Projeto não encontrado")
        );

        projectUnfilledRoleService.removeRelation(project.getId(), member.getRoleId());

        // Não é boa ideia usar hardcode, MAS… Deixei como Nome invés de LONG(id), ou agora, UUID(id), por clareza.
        // 2L antes, sem ler o flyway, não fazia muito sentido apenas lendo o código. (REF: V3__insert_project_tables.sql)
        MemberStatus approvedStatus = memberStatusService.findByName("Aprovado")
                .orElseThrow(() -> new CustomNotFoundException("Status Aprovado não encontrado"));

        member.setStatusId(approvedStatus.getId());
        repository.save(member);

        return Optional.of(member);
    }

    // Não é boa ideia usar hardcode, MAS… Deixei como Nome invés de LONG(id), ou agora, UUID(id), por clareza.
    // 1L antes, sem ler o flyway, não fazia muito sentido apenas lendo o código. (REF: V3__insert_project_tables.sql)
    @Transactional
    public Optional<Member> reject(Long memberId, String applicationFeedback, UUID userId) {
        Member member = repository.findById(memberId).orElseThrow(() ->
                new CustomNotFoundException("Membro não encontrado")
        );

        MemberStatus pendingStatus = memberStatusService.findByName("Pendente").orElseThrow(() ->
                new CustomNotFoundException("Status Pendente não encontrado")
        );

        if (!member.getStatusId().equals(pendingStatus.getId())) {
            throw new ConflictException("Membro não está com status pendente");
        }

        Project project = projectRepository.findById(member.getProjectId()).orElseThrow(() ->
                new CustomNotFoundException("Projeto não encontrado")
        );

        if (!isUserProjectManager(project.getId(), userId)) {
            throw new UnauthorizedException("Usuário que solicitou alteração não é o idealizador do projeto");
        }

        MemberStatus rejectedStatus = memberStatusService.findByName("Rejeitado")
                .orElseThrow(() -> new CustomNotFoundException("Status Rejeitado não encontrado"));

        member.setStatusId(rejectedStatus.getId());
        member.setApplicationFeedback(applicationFeedback);
        repository.save(member);

        return Optional.of(member);
    }

    // Métodos auxiliares, todos fazem parte da Transação caller, então nada de aninhar aqui com anotação de novo
    // Mantido aqui pela REF: https://github.com/m42hub/m42hub-api/blob/main/src/main/java/com/m42hub/m42hub_api/project/service/ProjectService.java#L212
    public boolean isUserProjectManager(UUID projectId, UUID userId) {
        Optional<Member> member = repository.findByProjectIdAndUserId(projectId, userId);
        return member.isPresent() && Boolean.TRUE.equals(member.get().getIsManager());
    }

    // Pode ser interessante mudar esta função de classe, assim como a de cima, pois esta é sobre Role, não sobre Status ou Membro.
    // Mantido aqui pela REF: https://github.com/m42hub/m42hub-api/blob/main/src/main/java/com/m42hub/m42hub_api/project/service/ProjectService.java#L219
    public boolean isRoleFilled(Project project, Long roleId) {
        Set<Long> unfilledRoles = projectUnfilledRoleService.getRoleIdsByProject(project.getId());
        return !unfilledRoles.contains(roleId);
    }
}