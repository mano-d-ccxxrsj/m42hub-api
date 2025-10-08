package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.Member;
import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.project.entity.Project;
import com.m42hub.m42hub_api.project.repository.MemberRepository;
import com.m42hub.m42hub_api.project.repository.ProjectRepository;
import com.m42hub.m42hub_api.project.service.MemberService;
import com.m42hub.m42hub_api.project.service.MemberStatusService;
import com.m42hub.m42hub_api.project.service.ProjectUnfilledRoleService;
import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class MemberServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(MemberServiceTest.class);

    private final int wantedNumberOfInvocations = 1;

    private static final Long PRIMARY_MEMBER_ID = 1L;
    private static final Long SECONDARY_MEMBER_ID = 2L;
    private static final Long PENDING_MEMBER_ID = 3L;

    private final UUID PRIMARY_PROJECT_ID = TestUtils.getRandomUUID();
    private final UUID SECONDARY_PROJECT_ID = TestUtils.getRandomUUID();

    private final UUID PRIMARY_USER_ID = TestUtils.getRandomUUID();
    private final UUID SECONDARY_USER_ID = TestUtils.getRandomUUID();

    private static final Long PRIMARY_ROLE_ID = 1L;
    private static final Long SECONDARY_ROLE_ID = 2L;

    private static final Long PRIMARY_STATUS_APPROVED_ID = 1L;
    private static final Long PRIMARY_STATUS_PENDING_ID = 2L;
    private static final Long PRIMARY_STATUS_REJECTED_ID = 3L;

    private static final String PRIMARY_APPLICATION_MESSAGE = "I want to join this project";
    private static final String PRIMARY_APPLICATION_FEEDBACK = "Your application was rejected";

    private static final String PRIMARY_USERNAME = "primaryuser";
    private static final String PRIMARY_FIRST_NAME = "Primary";
    private static final String PRIMARY_LAST_NAME = "User";
    private static final String PRIMARY_EMAIL = "primary@test.com";
    private static final String PRIMARY_PASSWORD = "password";
    private static final String PRIMARY_PROFILE_PIC = "profile_pic_url";
    private static final String PRIMARY_PROFILE_BANNER = "profile_banner_url";
    private static final String PRIMARY_BIO = "biography";
    private static final String PRIMARY_DISCORD = "discord";
    private static final String PRIMARY_LINKEDIN = "linkedin";
    private static final String PRIMARY_GITHUB = "github";
    private static final String PRIMARY_WEBSITE = "personal_website";

    private static final String PRIMARY_PROJECT_NAME = "PrimaryProject";
    private static final String PRIMARY_PROJECT_SUMMARY = "Summary";
    private static final String PRIMARY_PROJECT_DESC = "Description";
    private static final String PRIMARY_PROJECT_IMAGE = "image_url";
    private static final String PRIMARY_PROJECT_WEBSITE = "project_website";

    private static final String PRIMARY_STATUS_PENDING_NAME = "Pendente";
    private static final String PRIMARY_STATUS_PENDING_DESC = "Aguardando aprovação";
    private static final String PRIMARY_STATUS_APPROVED_NAME = "Aprovado";
    private static final String PRIMARY_STATUS_APPROVED_DESC = "Membro aprovado";
    private static final String PRIMARY_STATUS_REJECTED_NAME = "Rejeitado";
    private static final String PRIMARY_STATUS_REJECTED_DESC = "Membro rejeitado";

    private static final Boolean IS_ACTIVE = true;
    private static final LocalDateTime LAST_LOGIN = LocalDateTime.now();
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();
    private static final LocalDateTime UPDATED_AT = LocalDateTime.now();
    private static final Date START_DATE = new Date();
    private static final Date END_DATE = new Date();

    @Mock
    private ProjectUnfilledRoleService projectUnfilledRoleService;

    @Mock
    private MemberStatusService memberStatusService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MemberService memberService;

    private AutoCloseable mocks;

    private Member memberPrimary;
    private Member memberSecondary;
    private Member memberPending;
    private User primaryUser;
    private Project primaryProject;
    private MemberStatus statusPending;
    private MemberStatus statusApproved;
    private MemberStatus statusRejected;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        statusPending = TestUtils.createMemberStatus(PRIMARY_STATUS_PENDING_ID, PRIMARY_STATUS_PENDING_NAME, PRIMARY_STATUS_PENDING_DESC);
        statusApproved = TestUtils.createMemberStatus(PRIMARY_STATUS_APPROVED_ID, PRIMARY_STATUS_APPROVED_NAME, PRIMARY_STATUS_APPROVED_DESC);
        statusRejected = TestUtils.createMemberStatus(PRIMARY_STATUS_REJECTED_ID, PRIMARY_STATUS_REJECTED_NAME, PRIMARY_STATUS_REJECTED_DESC);

        primaryUser = TestUtils.createUser(
                PRIMARY_USER_ID,
                PRIMARY_USERNAME,
                PRIMARY_FIRST_NAME,
                PRIMARY_LAST_NAME,
                PRIMARY_EMAIL,
                PRIMARY_PASSWORD,
                PRIMARY_PROFILE_PIC,
                PRIMARY_PROFILE_BANNER,
                PRIMARY_BIO,
                PRIMARY_DISCORD,
                PRIMARY_LINKEDIN,
                PRIMARY_GITHUB,
                PRIMARY_WEBSITE,
                IS_ACTIVE,
                LAST_LOGIN,
                CREATED_AT,
                UPDATED_AT
        );

        primaryProject = TestUtils.createProject(
                PRIMARY_PROJECT_ID,
                PRIMARY_PROJECT_NAME,
                PRIMARY_PROJECT_SUMMARY,
                PRIMARY_PROJECT_DESC,
                PRIMARY_STATUS_PENDING_ID,
                PRIMARY_STATUS_PENDING_ID,
                PRIMARY_PROJECT_IMAGE,
                START_DATE,
                END_DATE,
                PRIMARY_DISCORD,
                PRIMARY_GITHUB,
                PRIMARY_PROJECT_WEBSITE,
                CREATED_AT,
                UPDATED_AT
        );

        memberPrimary = TestUtils.createMember(PRIMARY_MEMBER_ID, true, null, null);
        memberPrimary.setProjectId(PRIMARY_PROJECT_ID);
        memberPrimary.setRoleId(PRIMARY_ROLE_ID);
        memberPrimary.setUserId(PRIMARY_USER_ID);
        memberPrimary.setStatusId(PRIMARY_STATUS_APPROVED_ID);

        memberSecondary = TestUtils.createMember(SECONDARY_MEMBER_ID, false, null, null);
        memberSecondary.setProjectId(PRIMARY_PROJECT_ID);
        memberSecondary.setRoleId(SECONDARY_ROLE_ID);
        memberSecondary.setUserId(SECONDARY_USER_ID);
        memberSecondary.setStatusId(PRIMARY_STATUS_APPROVED_ID);

        memberPending = TestUtils.createMember(PENDING_MEMBER_ID, false, PRIMARY_APPLICATION_MESSAGE, null);
        memberPending.setProjectId(PRIMARY_PROJECT_ID);
        memberPending.setRoleId(SECONDARY_ROLE_ID);
        memberPending.setUserId(SECONDARY_USER_ID);
        memberPending.setStatusId(PRIMARY_STATUS_PENDING_ID);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldSaveMemberSucceed() {
        // GIVEN
        Member newMember = TestUtils.createMember(null, false, PRIMARY_APPLICATION_MESSAGE, null);
        newMember.setProjectId(PRIMARY_PROJECT_ID);
        newMember.setRoleId(PRIMARY_ROLE_ID);
        newMember.setUserId(PRIMARY_USER_ID);
        newMember.setStatusId(PRIMARY_STATUS_PENDING_ID);

        Mockito.when(memberRepository.save(newMember)).thenReturn(memberPrimary);

        // WHEN
        Member result = memberService.save(newMember);

        // THEN
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(PRIMARY_MEMBER_ID);
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).save(newMember);
    }

    @Test
    public void shouldReturnMember_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(memberRepository.findById(PRIMARY_MEMBER_ID)).thenReturn(Optional.of(memberPrimary));

        // WHEN
        Optional<Member> result = memberService.findById(PRIMARY_MEMBER_ID);

        // THEN
        Assertions.assertThat(result).isPresent().containsSame(memberPrimary);
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).findById(PRIMARY_MEMBER_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 4L;
        Mockito.when(memberRepository.findById(invalidId)).thenReturn(Optional.empty());

        // WHEN
        Optional<Member> result = memberService.findById(invalidId);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).findById(invalidId);
    }

    @Test
    public void shouldReturnAllMembers_whenFindAllIsCalled() {
        // GIVEN
        List<Member> expectedMembers = List.of(memberPrimary, memberSecondary);
        Mockito.when(memberRepository.findAll()).thenReturn(expectedMembers);

        // WHEN
        List<Member> result = memberService.findAll();

        // THEN
        Assertions.assertThat(result).hasSize(2).containsExactlyInAnyOrder(memberPrimary, memberSecondary);
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).findAll();
    }

    @Test
    public void shouldReturnMembers_whenFindByUsernameIsCalled() {
        // GIVEN
        Mockito.when(userService.findByUsername(PRIMARY_USERNAME)).thenReturn(Optional.of(primaryUser));
        Mockito.when(memberRepository.findAllByUserId(PRIMARY_USER_ID)).thenReturn(List.of(memberPrimary));

        // WHEN
        List<Member> result = memberService.findByUsername(PRIMARY_USERNAME);

        // THEN
        Assertions.assertThat(result).hasSize(1).containsExactly(memberPrimary);
        Mockito.verify(userService, Mockito.times(wantedNumberOfInvocations)).findByUsername(PRIMARY_USERNAME);
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).findAllByUserId(PRIMARY_USER_ID);
    }

    @Test
    public void shouldReturnMembers_whenFindByProjectIdIsCalled() {
        // GIVEN
        Mockito.when(memberRepository.findAllByProjectId(PRIMARY_PROJECT_ID)).thenReturn(List.of(memberPrimary, memberSecondary));

        // WHEN
        List<Member> result = memberService.findByProjectId(PRIMARY_PROJECT_ID);

        // THEN
        Assertions.assertThat(result).hasSize(2).containsExactlyInAnyOrder(memberPrimary, memberSecondary);
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).findAllByProjectId(PRIMARY_PROJECT_ID);
    }

    @Test
    public void shouldReturnMembersByProjectIds_whenFindByProjectIdsIsCalled() {
        // GIVEN
        List<UUID> projectIds = List.of(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID);
        Mockito.when(memberRepository.findByProjectIdIn(projectIds)).thenReturn(List.of(memberPrimary, memberSecondary));

        // WHEN
        Map<UUID, List<Member>> result = memberService.findByProjectIds(projectIds);

        // THEN
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(PRIMARY_PROJECT_ID)).hasSize(2).containsExactlyInAnyOrder(memberPrimary, memberSecondary);
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).findByProjectIdIn(projectIds);
    }

    @Test
    public void shouldApproveMemberSucceed_whenUserIsManagerAndRoleNotFilled() {
        // GIVEN
        Mockito.when(memberRepository.findById(memberPending.getId())).thenReturn(Optional.of(memberPending));
        Mockito.when(projectRepository.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(primaryProject));
        Mockito.when(memberRepository.findByProjectIdAndUserId(PRIMARY_PROJECT_ID, PRIMARY_USER_ID)).thenReturn(Optional.of(memberPrimary));
        Mockito.when(projectUnfilledRoleService.getRoleIdsByProject(PRIMARY_PROJECT_ID)).thenReturn(Set.of(SECONDARY_ROLE_ID));
        Mockito.when(memberStatusService.findByName(PRIMARY_STATUS_APPROVED_NAME)).thenReturn(Optional.of(statusApproved));
        Mockito.when(memberRepository.save(memberPending)).thenReturn(memberPending);

        // WHEN
        Optional<Member> result = memberService.approve(memberPending.getId(), PRIMARY_USER_ID);

        // THEN
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(memberPending.getStatusId()).isEqualTo(PRIMARY_STATUS_APPROVED_ID);
        Mockito.verify(projectUnfilledRoleService, Mockito.times(wantedNumberOfInvocations)).removeRelation(PRIMARY_PROJECT_ID, SECONDARY_ROLE_ID);
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).save(memberPending);
    }

    @Test
    public void shouldThrowException_whenApproveMemberWithoutPermission() {
        // GIVEN
        Mockito.when(memberRepository.findById(memberPending.getId())).thenReturn(Optional.of(memberPending));
        Mockito.when(projectRepository.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(primaryProject));
        Mockito.when(memberRepository.findByProjectIdAndUserId(PRIMARY_PROJECT_ID, PRIMARY_USER_ID)).thenReturn(Optional.empty());

        // WHEN & THEN
        Assertions.assertThatThrownBy(() -> memberService.approve(memberPending.getId(), PRIMARY_USER_ID))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuário que solicitou alteração não é o idealizador do projeto");
        Mockito.verify(memberRepository, Mockito.never()).save(Mockito.any(Member.class));
    }

    @Test
    public void shouldRejectMemberSucceed_whenUserIsManager() {
        // GIVEN
        Mockito.when(memberRepository.findById(memberPending.getId())).thenReturn(Optional.of(memberPending));
        Mockito.when(memberStatusService.findByName(PRIMARY_STATUS_PENDING_NAME)).thenReturn(Optional.of(statusPending));
        Mockito.when(projectRepository.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(primaryProject));
        Mockito.when(memberRepository.findByProjectIdAndUserId(PRIMARY_PROJECT_ID, PRIMARY_USER_ID)).thenReturn(Optional.of(memberPrimary));
        Mockito.when(memberStatusService.findByName(PRIMARY_STATUS_REJECTED_NAME)).thenReturn(Optional.of(statusRejected));
        Mockito.when(memberRepository.save(memberPending)).thenReturn(memberPending);

        // WHEN
        Optional<Member> result = memberService.reject(memberPending.getId(), PRIMARY_APPLICATION_FEEDBACK, PRIMARY_USER_ID);

        // THEN
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(memberPending.getStatusId()).isEqualTo(PRIMARY_STATUS_REJECTED_ID);
        Assertions.assertThat(memberPending.getApplicationFeedback()).isEqualTo(PRIMARY_APPLICATION_FEEDBACK);
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).save(memberPending);
    }

    @Test
    public void shouldReturnTrue_whenUserIsProjectManager() {
        // GIVEN
        Mockito.when(memberRepository.findByProjectIdAndUserId(PRIMARY_PROJECT_ID, PRIMARY_USER_ID)).thenReturn(Optional.of(memberPrimary));

        // WHEN
        boolean result = memberService.isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID);

        // THEN
        Assertions.assertThat(result).isTrue();
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).findByProjectIdAndUserId(PRIMARY_PROJECT_ID, PRIMARY_USER_ID);
    }

    @Test
    public void shouldReturnFalse_whenUserIsNotProjectManager() {
        // GIVEN
        Mockito.when(memberRepository.findByProjectIdAndUserId(PRIMARY_PROJECT_ID, SECONDARY_USER_ID)).thenReturn(Optional.of(memberSecondary));

        // WHEN
        boolean result = memberService.isUserProjectManager(PRIMARY_PROJECT_ID, SECONDARY_USER_ID);

        // THEN
        Assertions.assertThat(result).isFalse();
        Mockito.verify(memberRepository, Mockito.times(wantedNumberOfInvocations)).findByProjectIdAndUserId(PRIMARY_PROJECT_ID, SECONDARY_USER_ID);
    }

    @Test
    public void shouldReturnTrue_whenRoleIsFilled() {
        // GIVEN
        Mockito.when(projectUnfilledRoleService.getRoleIdsByProject(PRIMARY_PROJECT_ID)).thenReturn(Set.of(PRIMARY_ROLE_ID));

        // WHEN
        boolean result = memberService.isRoleFilled(primaryProject, SECONDARY_ROLE_ID);

        // THEN
        Assertions.assertThat(result).isTrue();
        Mockito.verify(projectUnfilledRoleService, Mockito.times(wantedNumberOfInvocations)).getRoleIdsByProject(PRIMARY_PROJECT_ID);
    }

    @Test
    public void shouldReturnFalse_whenRoleIsNotFilled() {
        // GIVEN
        Mockito.when(projectUnfilledRoleService.getRoleIdsByProject(PRIMARY_PROJECT_ID)).thenReturn(Set.of(SECONDARY_ROLE_ID));

        // WHEN
        boolean result = memberService.isRoleFilled(primaryProject, SECONDARY_ROLE_ID);

        // THEN
        Assertions.assertThat(result).isFalse();
        Mockito.verify(projectUnfilledRoleService, Mockito.times(wantedNumberOfInvocations)).getRoleIdsByProject(PRIMARY_PROJECT_ID);
    }
}