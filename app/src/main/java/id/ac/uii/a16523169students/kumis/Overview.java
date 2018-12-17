package id.ac.uii.a16523169students.kumis;

/**
 * Data model for each row of the RecyclerView
 */
class Overview {

    // Member variables representing the title and information about the sport.
    private String title;
    private String info;
    private int imageResource;

    /**
     * Constructor for the Sport data model.
     *
     * @param title The name if the sport.
     * @param info Information about the sport.
     */
    public Overview(String title, String info, int imageResource) {
        this.title = title;
        this.info = info;
        this.imageResource = imageResource;
    }
    public int getImageResource() {
        return imageResource;
    }

    Overview(String title, String info) {
        this.title = title;
        this.info = info;
    }
    /**
     * Gets the title of the sport.
     *
     * @return The title of the sport.
     */
    String getTitle() {
        return title;
    }

    /**
     * Gets the info about the sport.
     *
     * @return The info about the sport.
     */
    String getInfo() {
        return info;
    }
}
