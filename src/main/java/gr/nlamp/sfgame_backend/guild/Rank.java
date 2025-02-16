package gr.nlamp.sfgame_backend.guild;

import java.util.List;

public enum Rank {
    LEADER,
    OFFICER,
    MEMBER;

    public static final List<Rank> CAN_SEND_INVITATION = List.of(LEADER, OFFICER);

    public static final List<Rank> CAN_UPGRADE_TREASURE_AND_INSTRUCTOR = List.of(LEADER, OFFICER);

    public static final List<Rank> CAN_UPDATE_DESCRIPTION = List.of(LEADER, OFFICER);
}
