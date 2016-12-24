package com.project.gtps.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by suresh on 12/23/16.
 */

@Entity
@Table(name = "user")
public class User implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", length = 50, nullable = false)
    private String userName;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "is_delete")
    private Integer isDelete;


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "user_device",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "device_id"))
    private List<Device> devices;

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer delete) {
        this.isDelete = delete;
    }
}
