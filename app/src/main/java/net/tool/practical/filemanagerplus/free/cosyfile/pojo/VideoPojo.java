package net.tool.practical.filemanagerplus.free.cosyfile.pojo;

public class VideoPojo {
    private String videoPath;
    private String videoName;
    private Boolean isSelect = false;

    public VideoPojo(String videoPath, String videoName) {
        this.videoPath = videoPath;
        this.videoName = videoName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
