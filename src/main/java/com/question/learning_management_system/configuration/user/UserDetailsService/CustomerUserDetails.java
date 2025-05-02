package com.question.learning_management_system.configuration.user.UserDetailsService;

import com.question.learning_management_system.entity.Instructor;
import com.question.learning_management_system.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerUserDetails implements UserDetails {

    private Long id;
    private String email;
    private String password;

    private Collection<GrantedAuthority> authorities;

    public static CustomerUserDetails fromStudent(Student student) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(student.getRole().name());
        return new CustomerUserDetails(
                student.getId(),
                student.getEmail(),
                student.getPassword(),
                List.of(authority)
        );
    }

    public static CustomerUserDetails fromInstructor(Instructor instructor) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(instructor.getRole().name());
        return new CustomerUserDetails(
                instructor.getId(),
                instructor.getEmail(),
                instructor.getPassword(),
                List.of(authority)
        );
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
