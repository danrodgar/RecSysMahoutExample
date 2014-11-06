/*
 =============
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package recommender;

public class App
{
    public static void main(String args[])
    {
        int id = 45;

        RecommenderSamples recommenderSamples = new RecommenderSamples();

        String users = recommenderSamples.recommendUsers(id).toString();
        String usersGeneric = recommenderSamples.recommendUsersGeneric(id).toString();
        String content = recommenderSamples.recommendContentWithUserid(id).toString();
        String contentGeneric = recommenderSamples.recommendContentWithUseridGeneric(id).toString();

        System.out.println();
        System.out.println("\t USERS: " + users);
        System.out.println("\t USERS_GENERIC: " + usersGeneric);
        System.out.println("\t CONTENT: " + content);
        System.out.println("\t CONTENT_GENERIC: " + contentGeneric);
        
        System.out.println();
    }
}