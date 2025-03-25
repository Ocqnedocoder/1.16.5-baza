package ru.levelup.client.protect;

import net.minecraft.pathfinding.Path;
import ru.levelup.client.api.utils.misc.WebUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class UserData {
    private static UserData get = new UserData();
    public String uid;
    public String name;
    public String hwid;
    public String role;
    public String time;
    public String gmail;
    public String password;
    public String avatar;
    public String DLC;

    public UserData() {
        get = this;
        String url = WebUtils.visitSite("http://ger2-1.deploy.sbs:1684/users?hwid=" + WebUtils.getFormattedHWID());
        try {
            String[] values = url.split("###");
            uid = values[0];
            name = values[1];
            hwid = values[2];
            time = values[3];
            role = values[4];
            gmail = values[5];
            password = values[6];
            DLC = values[7];
            avatar = values[8];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static UserData getGet() {
        return get;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getHwid() {
        return hwid;
    }

    public String getRole() {
        return role;
    }

    public String getTime() {
        return time;
    }

    public String getGmail() {
        return gmail;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDLC() {
        return DLC;
    }

    public static LocalDate convertToDate(long timeInHours) {
        long timeInMillis = timeInHours * 60 * 60 * 1000;
        Instant currentInstant = Instant.now();
        Instant resultInstant = currentInstant.plusMillis(timeInMillis);
        LocalDate date = resultInstant.atZone(ZoneId.systemDefault()).toLocalDate();

        return date;
    }
}
