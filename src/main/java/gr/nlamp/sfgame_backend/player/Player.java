package gr.nlamp.sfgame_backend.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.nlamp.sfgame_backend.guild.GuildInvitation;
import gr.nlamp.sfgame_backend.guild.GuildMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "players")
@Getter
@Setter
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false, updatable = false)
    private String username;
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    private String description;
    private Long honor;
    private Long highestRank;
    private Integer highestActiveFor; // in days
    private Integer activeFor; // in days
    private Long numberOfSuccessQuests;
    private Long strength;
    private Long dexterity;
    private Long intelligence;
    private Long constitution;
    private Long luck;
    // Potions
    private Boolean banned;
    private Boolean emailActivation;
    private Long emailActivationDate;
    private Long lastLoginDate;
    private Class playerClass;
    private Race race;
    private Gender gender;
    private Long silver; // 100 silver = 1 coin
    private Long mushrooms;
    private Long level;
    private Long experience;
    // Mount
    private Long mountTime; // when mount bought
    // Mirror pieces
    // Album
    // Tavern
    // Guild
    @OneToOne(mappedBy = "player")
    private GuildMember guildMember;
    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<GuildInvitation> guildInvitations = new HashSet<>();
    // Friends
    // Messages
    // FightLog
    // Bag
    // Equipment
    // Pets
    // Fortress
    // WeaponShop
    // MagicShop
    private Boolean goldenFrame;
    // Face
}
