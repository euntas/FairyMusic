package com.music.fairy.fairymusic.ui.result;

/**
 * Created by kita on 2017-07-27.
 */

public class FBResource {
    private int resourceNum;			// 리소스 번호
    private String name;		// 리소스 이름
    private String analysis;	// 리소스 설명
    private String path;		// 리소스 이미지 경로

    public FBResource() {
        // TODO Auto-generated constructor stub
    }

    public FBResource(int resourceNum, String name, String analysis) {
        super();
        this.resourceNum = resourceNum;
        this.name = name;
        this.analysis = analysis;
    }

    public FBResource(int resourceNum, String name, String analysis, String path) {
        super();
        this.resourceNum = resourceNum;
        this.name = name;
        this.analysis = analysis;
        this.path = path;
    }

    public int getResourceNum() {
        return resourceNum;
    }

    public void setResourceNum(int resourceNum) {
        this.resourceNum = resourceNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "FBResource [resourceNum=" + resourceNum + ", name=" + name + ", analysis=" + analysis + ", path=" + path
                + "]";
    }
}
