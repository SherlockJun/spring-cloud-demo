package com.gavin.business.domain;

import com.gavin.common.enums.UserStatusEnums;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@DynamicInsert
@DynamicUpdate
@Data
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "password")
    private String password;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatusEnums status;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserAuthority> userAuthorities;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "created_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    public UserAuthority addUserAuthority(UserAuthority userAuthority) {
        if (CollectionUtils.isEmpty(userAuthorities)) {
            userAuthorities = new ArrayList<>();
        }
        userAuthorities.add(userAuthority);
        userAuthority.setUser(this);

        return userAuthority;
    }

    public UserAuthority removeUserAuthority(UserAuthority userAuthority) {
        userAuthorities.remove(userAuthority);
        userAuthority.setUser(null);

        return userAuthority;
    }

}
