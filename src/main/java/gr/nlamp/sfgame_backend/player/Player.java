package gr.nlamp.sfgame_backend.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.nlamp.sfgame_backend.guild.GuildInvitation;
import gr.nlamp.sfgame_backend.guild.GuildMember;
import gr.nlamp.sfgame_backend.guild.GuildMessage;
import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.magic_mirror.MagicMirror;
import gr.nlamp.sfgame_backend.tavern.Tavern;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigInteger;
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
    private BigInteger honor;
    private Long highestRank;
    private Integer highestActiveFor; // in days
    private Integer activeFor; // in days
    private Long numberOfSuccessQuests;
    private BigInteger strength;
    private BigInteger dexterity;
    private BigInteger intelligence;
    private BigInteger constitution;
    private BigInteger luck;
    private Boolean banned;
    private Boolean emailActivation;
    private Long emailActivationDate;
    private Long lastLoginDate;
    private Class playerClass;
    private Race race;
    private Gender gender;
    private BigInteger silver; // 100 silver = 1 coin
    private Long mushrooms;
    private Long level;
    private BigInteger experience;
    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<Booster> boosters = new HashSet<>();
    // Mount
    private Long mountTime; // when mount bought
    @OneToOne(mappedBy = "player")
    private MagicMirror magicMirror;
    // Album
    @OneToOne(mappedBy = "player")
    private Tavern tavern;
    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<GuildMember> guildMembers = new HashSet<>();
    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<GuildInvitation> guildInvitations = new HashSet<>();
    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<GuildMessage> guildMessages = new HashSet<>();
    // Friends
    // Messages
    // FightLog
    // Pets
    // Fortress
    private Boolean goldenFrame;
    // Face
    // Achievements
    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<Item> items = new HashSet<>();
}
