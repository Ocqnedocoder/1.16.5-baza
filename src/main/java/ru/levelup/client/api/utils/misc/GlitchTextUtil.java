package ru.levelup.client.api.utils.misc;

public class GlitchTextUtil {
    public TimerUtil timer = new TimerUtil();
    public boolean anim;

    public String getAnim(String text, float speed, int delay) {
        int time = (int)(this.timer.getMc() / (long)((int)(speed * 1000.0f)) % text.chars().count());
        int delayTime = (int)(this.timer.getMc() / (long)((int)(speed * 1000.0f)) % (text.chars().count() * (long)(++delay))) + 1;
        char[] symbols = new char[]{'%', '@', '&', '\\', '/', '=', '$'};
        StringBuffer result = new StringBuffer(text);
        boolean bl = this.anim = (long)delayTime <= text.chars().count();
        if (this.anim) {
            result.setCharAt(time, symbols[(int)MathUtils.random(0.0, symbols.length)]);
        }
        return result.toString();
    }

    public String getAnimXCross(String text, float speed, int delay) {
        int time = (int)(this.timer.getMc() / (long)((int)(speed * 1000.0f)) % text.chars().count());
        int time2 = (int)(text.chars().count() - 1L - this.timer.getMc() / (long)((int)(speed * 1000.0f)) % text.chars().count());
        int delayTime = (int)(this.timer.getMc() / (long)((int)(speed * 1000.0f)) % (text.chars().count() * (long)(++delay))) + 1;
        char[] symbols = new char[]{'%', '@', '&', '\\', '/', '=', '$'};
        StringBuffer result = new StringBuffer(text);
        boolean bl = this.anim = (long)delayTime <= text.chars().count();
        if (this.anim) {
            result.setCharAt(time, symbols[(int) MathUtils.random(0.0, symbols.length)]);
        }
        if (this.anim) {
            result.setCharAt(time2, symbols[(int)MathUtils.random(0.0, symbols.length)]);
        }
        return result.toString();
    }

    public char[] getAnim(char[] text, float speed, int delay) {
        int time = (int)(this.timer.getMc() / (long)((int)(speed * 1000.0f)) % (long)text.length);
        int delayTime = (int)(this.timer.getMc() / (long)((int)(speed * 1000.0f)) % (long)(text.length * ++delay)) + 1;
        char[] symbols = new char[]{'%', '@', '&', '\\', '/', '=', '$'};
        char[] result = text;
        boolean bl = this.anim = delayTime <= text.length;
        if (this.anim) {
            result[time] = symbols[(int)MathUtils.random(0.0, symbols.length)];
        }
        return result;
    }

    public static String anim(String text, float speed, int delay) {
        int time = (int)(System.currentTimeMillis() / (long)((int)(speed * 1000.0f)) % text.chars().count());
        int delayTime = (int)(System.currentTimeMillis() / (long)((int)(speed * 1000.0f)) % (text.chars().count() * (long)(++delay))) + 1;
        char[] symbols = new char[]{'%', '@', '&', '\\', '/', '=', '$'};
        StringBuffer result = new StringBuffer(text);
        if ((long)delayTime <= text.chars().count()) {
            result.setCharAt(time, symbols[(int)MathUtils.random(0.0, symbols.length)]);
        }
        return result.toString();
    }

    public static String animXCross(String text, float speed, int delay) {
        int time = (int)(System.currentTimeMillis() / (long)((int)(speed * 1000.0f)) % text.chars().count());
        int time2 = (int)(text.chars().count() - 1L - System.currentTimeMillis() / (long)((int)(speed * 1000.0f)) % text.chars().count());
        int delayTime = (int)(System.currentTimeMillis() / (long)((int)(speed * 1000.0f)) % (text.chars().count() * (long)(++delay))) + 1;
        char[] symbols = new char[]{'%', '@', '&', '\\', '/', '=', '$'};
        StringBuffer result = new StringBuffer(text);
        if ((long)delayTime <= text.chars().count()) {
            result.setCharAt(time, symbols[(int)MathUtils.random(0.0, symbols.length)]);
        }
        if ((long)delayTime <= text.chars().count()) {
            result.setCharAt(time2, symbols[(int)MathUtils.random(0.0, symbols.length)]);
        }
        return result.toString();
    }
    public static String replaceAnim(String text, float speed, int delay, boolean line) {
        delay /= speed;
        int size = (int) text.chars().count();
        int totalTime = (int)(System.currentTimeMillis() / (long)((int)(speed * 1000.0f))
                % ((size + delay) + 1));

        int currentSize = 0;

        if (totalTime <= size) {
            StringBuffer res = new StringBuffer(text);
            res.setLength((int) MathUtils.clamp(size - totalTime, 0, size));

            if (line && (System.currentTimeMillis() / (long)(200)) % 6 < 3) res.append("_");

            return res.toString();
        }

        currentSize += delay;

        if (totalTime <= currentSize) {
            int removedChars = (totalTime - currentSize) / delay;
            StringBuffer res = new StringBuffer(text.substring(0, size - removedChars));

            if (line && (System.currentTimeMillis() / (long)(200)) % 6 < 3) res.append("_");

            return res.toString();
        }
        return "";
    }
    public static String replaceAnim(String text1, String text2, float speed, int delay, boolean line) {
        delay /= speed;
        int size1 = (int) text1.chars().count(), size2 = (int) text2.chars().count();
        int time = (int)(System.currentTimeMillis() / (long)((int)(speed * 1000.0f)) % (((size1 + size2)*2+delay*2)+1));
        StringBuffer res = new StringBuffer();
        if(time <= size1) {
            res = new StringBuffer(text1);
            res.setLength((int) MathUtils.clamp(size1 - time, 0, size1));
        }
        else if(time <= size1+size2+delay){
            res = new StringBuffer(text2);
            res.setLength((int) MathUtils.clamp(time-size1, 0, size2));
        }
        else if(time <= size1+size2*2+delay){
            res = new StringBuffer(text2);
            res.setLength((int) MathUtils.clamp(((size1+size2*2+delay)-time), 0, size2));
        }
        else if(time <= (size1+size2+delay)*2){
            res = new StringBuffer(text1);
            res.setLength((int) MathUtils.clamp((time-(size1+size2*2+delay)), 0, size1));
        }
        if(line && (System.currentTimeMillis() / (long)(200)) % 6 < 3)res.append("_");
        return res.toString();
    }
}

