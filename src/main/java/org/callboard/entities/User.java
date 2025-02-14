package org.callboard.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.callboard.annotations.NameValidation;
import org.callboard.annotations.StringFormatValidation;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @StringFormatValidation(groups = NameValidation.class)
    private String firstName;

    @NotBlank
    @StringFormatValidation(groups = NameValidation.class)
    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    @Size(min = 5, max = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    @JsonManagedReference
    private Set<Role> roles;


    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getClass() : o.getClass();
        Class<?> thisEffectiveCLass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getClass() : this.getClass();
        if (thisEffectiveCLass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != 0 && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getClass().hashCode() : this.getClass().hashCode();
    }
}
