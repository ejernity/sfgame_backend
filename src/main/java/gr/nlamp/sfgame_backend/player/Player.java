package gr.nlamp.sfgame_backend.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.nlamp.sfgame_backend.guild.GuildInvitation;
import gr.nlamp.sfgame_backend.guild.GuildMember;
import gr.nlamp.sfgame_backend.guild.GuildMessage;
import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.item.booster.Booster;
import gr.nlamp.sfgame_backend.magic_mirror.MagicMirror;
import gr.nlamp.sfgame_backend.stable.Mount;
import gr.nlamp.sfgame_backend.tavern.Quest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "players")
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String username;

    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private String description;

    private BigInteger honor;

    private Long currentRank;
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

    private Boolean emailActivation = false;
    private Long emailActivationDate;

    private Long lastLoginDate;

    private Long lastTavernAccessDate;

    private Long lastWeaponShopAccessDate;

    private Long lastMagicShopAccessDate;

    @Column(columnDefinition = "ENUM('WARRIOR','MAGE','SCOUT')")
    @Enumerated(value = EnumType.STRING)
    private Class playerClass;

    @Column(columnDefinition = "ENUM('HUMAN','ELF','DWARF','GNOME','ORC','DARC_ELF','GOBLIN','DEMON')")
    @Enumerated(value = EnumType.STRING)
    private Race race;

    @Column(columnDefinition = "ENUM('MALE','FEMALE')")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "ENUM('IDLE','QUEST','CITY_GUARD')")
    @Enumerated(value = EnumType.STRING)
    private PlayerState playerState;

    private BigInteger coins;

    private Long mushrooms;

    private Long level;

    private BigInteger gainedExperience;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<Booster> boosters = new HashSet<>();

    @Column(columnDefinition = "ENUM('PIG','DONKEY','TIGER','DRAGON')")
    @Enumerated(value = EnumType.STRING)
    private Mount mount;
    private Long mountActiveUntil; // when mount bought

    @OneToOne(mappedBy = "player")
    private MagicMirror magicMirror;

    // Album

    @Column(precision = 5, scale = 2)
    private BigDecimal currentEnergy; // min = 0, max = 100

    @Column(precision = 5, scale = 2)
    private BigDecimal totalUsedEnergy; // total day's energy (100 + 200 from beers)

    private Integer totalBeersDrink; // min=0, max=10

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<Quest> quests = new HashSet<>();

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

    @ManyToMany
    @JoinTable(name = "friends",
            joinColumns = {@JoinColumn(name = "player_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")})
    private List<Player> friends = new ArrayList<>();

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
