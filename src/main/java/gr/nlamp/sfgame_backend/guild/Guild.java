package gr.nlamp.sfgame_backend.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "guilds")
@Getter
@Setter
public class Guild implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false, updatable = false)
    private String name;
    private String description;
    private BigInteger honor;
    private Integer treasureLevel;
    private Integer instructorLevel;
    @OneToMany(mappedBy = "guild", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<GuildInvitation> guildInvitations = new HashSet<>();
    @OneToMany(mappedBy = "guild", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<GuildMember> guildMembers = new HashSet<>();
    @OneToMany(mappedBy = "guild", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<GuildMessage> guildMessages = new HashSet<>();
}
