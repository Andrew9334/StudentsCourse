package сom.custis.university.dto;

public class ReviewDTO {

    private String id;
    private String studentId;
    private String courseId;
    private String content;

    public ReviewDTO() {
    }

    public ReviewDTO(String id, String studentId, String courseId, String content) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
