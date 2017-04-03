/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talic.filmovi.units;

/**
 *
 * @author Talic
 */
public class Movie {

   private String title;
   private String link;

   public Movie() {

   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setLink(String link) {
      this.link = link;
   }

   public String getTitle() {
      return title;
   }

   public String getLink() {
      return link;
   }
}
