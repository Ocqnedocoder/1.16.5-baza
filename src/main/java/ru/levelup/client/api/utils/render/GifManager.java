package ru.levelup.client.api.utils.render;

import java.util.ArrayList;

public class GifManager {
    private ArrayList<Gif> gifs = new ArrayList();
    public Gif enemy;
    public Gif jumpCircle1;
    //public Gif jumpCircle2;

    public GifManager() {

    }

    public void init() {

//        gifs.add(new Gif("https://cdn.discordapp.com/attachments/1148683101485146205/1200728116419629066/cool-sunglasses.gif"));
        jumpCircle1 = new Gif("yy3.gif", 30);
        //jumpCircle2 = new Gif("GIF_NAME2.gif", 30);

    }

    public ArrayList<Gif> getGifs() {
        return gifs;
    }
}
