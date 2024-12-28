package com.example.ProsePetal.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(name = "post_title", length = 100, nullable = false)
    private String title;

    @Column(name = "post_content", columnDefinition = "MEDIUMTEXT",nullable = false)
    private String content;

    @Column(name="image",  columnDefinition = "MEDIUMTEXT")
    private String imageName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_date" , nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "user_Id", referencedColumnName = "user_Id")
    @NotNull
    private User user;
}