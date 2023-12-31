package com.ru.preferans.entities.user;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.move.Move;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String email;
    private String password;

    private String name;

    private int score;
    private boolean ready;

    @ManyToOne
    private Game game;

    @OneToMany
    private List<Move> moves;

    @ManyToMany
    private List<Card> cards;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
    public boolean isEnabled() {
        return true;
    }
}
