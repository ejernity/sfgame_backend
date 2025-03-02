package gr.nlamp.sfgame_backend.guild;

import java.util.List;
import java.util.Map;

public enum Rank {
    LEADER,
    OFFICER,
    MEMBER;

    public static final List<Rank> CAN_SEND_INVITATION = List.of(LEADER, OFFICER);

    public static final List<Rank> CAN_UPGRADE_TREASURE_AND_INSTRUCTOR = List.of(LEADER, OFFICER);

    public static final List<Rank> CAN_UPDATE_DESCRIPTION = List.of(LEADER, OFFICER);

    public static final List<Rank> CAN_UPDATE_RANK = List.of(LEADER, OFFICER);

    public static final Map<Rank, Rank> VALID_RANK_UPDATES = Map.of(OFFICER, MEMBER, MEMBER, OFFICER);

    public static final List<Rank> CAN_KICK_OFF_MEMBER = List.of(LEADER);
}
