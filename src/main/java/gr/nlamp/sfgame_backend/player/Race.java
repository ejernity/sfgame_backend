package gr.nlamp.sfgame_backend.player;

public enum Race {
    HUMAN(new int[]{0, 0, 0, 0, 0}),
    ELF(new int[]{-1, 2, 0, -1, 0}),
    DWARF(new int[]{0, -2, -1, 2, 1}),
    GNOME(new int[]{-2, 3, -1, -1, 1}),
    ORC(new int[]{1, 0, -1, 0, 0}),
    DARC_ELF(new int[]{-2, 2, 1, -1, 0}),
    GOBLIN(new int[]{-2, 2, 0, -1, 1}),
    DEMON(new int[]{3, -1, 0, 1, -3});

    // strength, dexterity, intelligence, constitution, luck
    private final int[] skills;

    Race(int[] skills) {
        this.skills = skills;
    }

    public int[] getSkills() {
        return skills;
    }

}
