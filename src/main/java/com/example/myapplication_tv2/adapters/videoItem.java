package com.example.myapplication_tv2.adapters;

public class videoItem {
    private int poster;      //海报图片
    private String title;       //视频名字
    private String info;    //视频简介  or  subtitle
    private int type;       //主页布局分类

    //第一类别下 所有视频 4*n
    public videoItem(int poster, String title, String info){
        this.poster = poster;
        this.title = title;
        this.info = info;
    }

    //给主页 不同布局的video们
    public videoItem(int poster, String title, String info,int type){
        this.poster = poster;
        this.title = title;
        this.info = info;
        this.type = type;
    }

    //给主页 子标题
    public videoItem(String subtitle, int type){
        this.info = subtitle;
        this.type =type;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfotext() {
        return info;
    }
    public void setInfotext(String infotext) {
        this.info = infotext;
    }

    public int getPoster() {
        return poster;
    }
    public void setPoster(int poster) {
        this.poster = poster;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DetailInfo{" +
                "mTitle='" + title + '\'' +
                ", mTextDesc=" + info +
                ", mPostImageUrl='" + poster + '\'' +
                '}';
    }
}
