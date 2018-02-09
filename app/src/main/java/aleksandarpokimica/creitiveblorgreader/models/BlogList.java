package aleksandarpokimica.creitiveblorgreader.models;

/**
 * Created by Aleksandar on 2/1/2018.
 */

public class BlogList {

    private String id;
    private String title;
    private String image_url;
    private String description;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
