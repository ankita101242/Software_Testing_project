package com.example.ProsePetal.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@Entity
@Table(name="Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails{

        public static User user;
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="user_Id", nullable = false)
        private Integer userId;

        @Column(name="Name")
        private String name;

        @Column(name="Email",nullable = false)
        private String email;

        @Column(name="Password", nullable = false)
        private String password;

        @Column(name="About", nullable = true)
        private String about;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Post> posts = new ArrayList<>();

        @Enumerated(EnumType.STRING)
        private Role role;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(role.name()));
        }

        @Override
        public String getUsername() {
                return email;
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return true;
        }

        @Override
        public String getPassword()
        {
                return password;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }

        public void setUsername(String email) {
                this.email=email;
        }

        public void setRoles(Role role) {
                this.role=role;
        }
}