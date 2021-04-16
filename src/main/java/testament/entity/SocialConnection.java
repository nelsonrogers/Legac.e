/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
// Lombok
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class SocialConnection implements Serializable {
    private static final long serialVersionUID = -2218277427219576197L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    private String providerId;

    private String providerUserId;

    private int rank;

    private String displayName;

    @Column(length = 512)
    private String profileUrl;

    @Column(length = 512)
    private String imageUrl;

    @Column(length = 512)
    private String accessToken;

    @Column(length = 512)
    private String secret;

    @Column(length = 512)
    private String refreshToken;

    private Long expireTime;

    @ManyToOne
    @ToString.Exclude
    Utilisateur owner;
}