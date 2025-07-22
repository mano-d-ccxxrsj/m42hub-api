package com.m42hub.m42hub_api.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_members_id")
    private Long id;

    @Column(name = "is_manager", nullable = false)
    private String isManager;

    @ManyToOne()
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
    private Project project;

    @ManyToOne()
    @JoinColumn(name = "project_role_id", referencedColumnName = "project_role_id", nullable = false)
    private Role role;

//    @ManyToOne()
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
//    private User user;


}
