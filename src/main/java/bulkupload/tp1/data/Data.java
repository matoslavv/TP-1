package bulkupload.tp1.data;

public class Data {
    private String blobb;
    private String description;
    private String filetype;
    private Integer datagroupId;
    private Integer dlProtection;

    public String getBlobb() {
        return blobb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public Integer getDatagroupId() {
        return datagroupId;
    }

    public void setDatagroupId(Integer datagroupId) {
        this.datagroupId = datagroupId;
    }

    public Integer getDlProtection() {
        return dlProtection;
    }

    public void setDlProtection(Integer dlProtection) {
        this.dlProtection = dlProtection;
    }

    public void setBlobb(String blobb) {
        this.blobb = blobb;
    }
// Getters and setters
}
