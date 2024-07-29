package com.sparta.filmfly.global.auth;

import com.sparta.filmfly.domain.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class UserDetailsImpl implements UserDetails {

    /**
     * -- GETTER --
     *  사용자 객체 반환
     */
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    /**
     * 사용자 권한 목록 반환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = user.getUserRole().name();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);    // 권한목록에 권한 추가

        return authorities;
    }

    /**
     * 사용자 비밀번호 반환
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 사용자 이름 반환
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 계정 만료 여부 반환
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금 여부 반환
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명 만료 여부 반환
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부 반환
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
