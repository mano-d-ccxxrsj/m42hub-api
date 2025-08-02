package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.project.dto.request.MemberStatusRequest;
import com.m42hub.m42hub_api.project.dto.response.MemberStatusResponse;
import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.project.mapper.MemberStatusMapper;
import com.m42hub.m42hub_api.project.service.MemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project/member/status")
@RequiredArgsConstructor
public class MemberStatusController {

    private final MemberStatusService memberStatusService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('status:get_all')")
    public ResponseEntity<List<MemberStatusResponse>> getAll() {
        return ResponseEntity.ok(memberStatusService.findAll()
                .stream()
                .map(MemberStatusMapper::toMemberStatusResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('status:get_by_id')")
    public ResponseEntity<MemberStatusResponse> getById(@PathVariable Long id) {
        return memberStatusService.findById(id)
                .map(status -> ResponseEntity.ok(MemberStatusMapper.toMemberStatusResponse(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('status:create')")
    public ResponseEntity<MemberStatusResponse> save(@RequestBody MemberStatusRequest request) {
        MemberStatus newMemberStatus = MemberStatusMapper.toMemberStatus(request);
        MemberStatus savedMemberStatus = memberStatusService.save(newMemberStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(MemberStatusMapper.toMemberStatusResponse(savedMemberStatus));
    }


}
