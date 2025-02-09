package gr.nlamp.sfgame_backend.guild;

import java.util.List;

public enum Rank {
    LEADER,
    OFFICER,
    MEMBER;

    public static final List<Rank> CAN_SEND_INVITATION = List.of(LEADER, OFFICER);
}
