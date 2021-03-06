/*
 * API V1
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.client.model.Pagination;
import io.swagger.client.model.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Posts
 */


public class Posts implements Serializable {
  @SerializedName("posts")
  private List<Post> posts = null;

  @SerializedName("posts_pagination")
  private Pagination postsPagination = null;

  public Posts posts(List<Post> posts) {
    this.posts = posts;
    return this;
  }

  public Posts addPostsItem(Post postsItem) {
    if (this.posts == null) {
      this.posts = new ArrayList<Post>();
    }
    this.posts.add(postsItem);
    return this;
  }

   /**
   * Get posts
   * @return posts
  **/
  @Schema(description = "")
  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  public Posts postsPagination(Pagination postsPagination) {
    this.postsPagination = postsPagination;
    return this;
  }

   /**
   * Get postsPagination
   * @return postsPagination
  **/
  @Schema(description = "")
  public Pagination getPostsPagination() {
    return postsPagination;
  }

  public void setPostsPagination(Pagination postsPagination) {
    this.postsPagination = postsPagination;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Posts posts = (Posts) o;
    return Objects.equals(this.posts, posts.posts) &&
        Objects.equals(this.postsPagination, posts.postsPagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(posts, postsPagination);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Posts {\n");
    
    sb.append("    posts: ").append(toIndentedString(posts)).append("\n");
    sb.append("    postsPagination: ").append(toIndentedString(postsPagination)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
