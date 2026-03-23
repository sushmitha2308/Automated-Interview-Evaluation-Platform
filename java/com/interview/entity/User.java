package com.interview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String resumeUrl;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CodingSubmission> codingSubmissions = new ArrayList<>();

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VideoResponse> videoResponses = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Role {
        CANDIDATE, RECRUITER, ADMIN
    }

    // Constructors
    public User() {}

    public User(Long id, String email, String password, String fullName, Role role,
                String phone, String resumeUrl, boolean enabled,
                LocalDateTime createdAt, LocalDateTime updatedAt,
                List<CodingSubmission> codingSubmissions, List<VideoResponse> videoResponses) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.resumeUrl = resumeUrl;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.codingSubmissions = codingSubmissions != null ? codingSubmissions : new ArrayList<>();
        this.videoResponses = videoResponses != null ? videoResponses : new ArrayList<>();
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }
    public String getPhone() { return phone; }
    public String getResumeUrl() { return resumeUrl; }
    public boolean isEnabled() { return enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<CodingSubmission> getCodingSubmissions() { return codingSubmissions; }
    public List<VideoResponse> getVideoResponses() { return videoResponses; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(Role role) { this.role = role; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setCodingSubmissions(List<CodingSubmission> codingSubmissions) { this.codingSubmissions = codingSubmissions; }
    public void setVideoResponses(List<VideoResponse> videoResponses) { this.videoResponses = videoResponses; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String email;
        private String password;
        private String fullName;
        private Role role;
        private String phone;
        private String resumeUrl;
        private boolean enabled = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<CodingSubmission> codingSubmissions = new ArrayList<>();
        private List<VideoResponse> videoResponses = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder resumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; return this; }
        public Builder enabled(boolean enabled) { this.enabled = enabled; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder codingSubmissions(List<CodingSubmission> val) { this.codingSubmissions = val; return this; }
        public Builder videoResponses(List<VideoResponse> val) { this.videoResponses = val; return this; }

        public User build() {
            User u = new User();
            u.id = this.id;
            u.email = this.email;
            u.password = this.password;
            u.fullName = this.fullName;
            u.role = this.role;
            u.phone = this.phone;
            u.resumeUrl = this.resumeUrl;
            u.enabled = this.enabled;
            u.createdAt = this.createdAt;
            u.updatedAt = this.updatedAt;
            u.codingSubmissions = this.codingSubmissions;
            u.videoResponses = this.videoResponses;
            return u;
        }
    }
}
