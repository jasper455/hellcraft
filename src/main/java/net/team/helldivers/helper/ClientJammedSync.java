package net.team.helldivers.helper;

public class ClientJammedSync {
    private static boolean isJammed = false;

    public static void setIsJammed(boolean jammed)  {
        isJammed = jammed;
    }

    public static boolean getIsJammed() {
        return isJammed;
    }

}
