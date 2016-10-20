package project3.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project3.dao.LikeAndDislikeDao;
import project3.dao.PostAndReplyDao;
import project3.dao.SimpleDao;
import project3.dto.Complex;
import project3.dto.DisLikeablePost;
import project3.dto.DisLikeableReply;
import project3.dto.ForumCategory;
import project3.dto.ForumPost;
import project3.dto.LikeablePost;
import project3.dto.LikeableReply;
import project3.dto.Person;
import project3.dto.PostReply;
import project3.util.GetTimestamp;

@SuppressWarnings("unused")
@Service
public class BusinessLogicImpl implements BusinessLogic {

	@Autowired
	Crypt crypt;

	@Autowired
	SimpleDao dao;
	
	@Autowired
	PostAndReplyDao postReplyDao;
	
	@Autowired
	LikeAndDislikeDao likeDislikeDao;

	public boolean checkUserPassword(String username, String password, String curpassword) {
		return crypt.validate(password, curpassword);
	}

	@Override
	public Person getPersonById(int id) {
		return dao.getPersonById(id);
	}

	@Override
	public Person getPersonByUsername(String username) {
		return dao.getPersonByUsername(username);
	}

	@Override
	@Transactional
	public String updateTempPerson(String username, String pass, String oldPass, String newUsername) {
		Person person = dao.getPersonByUsername(username);
		// System.out.println("ere" + username);
		Person checkUsername = dao.getPersonByUsername(newUsername);
		if (checkUsername == null) {
			if (crypt.validate(oldPass, person.getPassword())) {
				String encryptPass = crypt.encrypt(pass);
				dao.updateTempPerson(username, encryptPass, newUsername);
				return "[\"Updated\"]";
			} else {
				return "[\"Inputed Wrong Password\"]";
			}
		} else {
			return "[\"Username Already Exist\"]";
		}

	}

	@Override
	@Transactional
	public String updateUserInfo(Person person, String oldPassword, String newPassword, String username,
			String newEmail, String newPhone, String complex, String newUniversity, String newLinkedIn) {
		// password
		if (oldPassword != null && newPassword != null && !("".equals(newPassword) && !("".equals(newPassword)))) {
			if (checkUserPassword(person.getUsername(), oldPassword, person.getPassword())) {
				person.setPassword(crypt.encrypt(newPassword));
			} else {
				return "[\"Inputed Wrong Password\"]";
			}
		}
		// username
		if (username != null && !("".equals(username))) {
			Person checkUsername = dao.getPersonByUsername(username);
			if (checkUsername == null) {
				person.setUsername(username);
			} else {
				return "[\"Username Already Exist\"]";
			}
		}
		// email
		if (newEmail != null && !("".equals(newEmail))) {
			person.setEmail(newEmail);
		}
		// phone
		if (newPhone != null && !("".equals(newPhone))) {
			person.setPhoneNumber(newPhone);
		}
		//complex
		System.out.println("------------------------------here");
		if (complex != null && !("".equals(complex))) {
			System.out.println(dao.getComplexByName(complex));
			person.setComplex(dao.getComplexByName(complex));
		}
		// university
		if (newUniversity != null && !("".equals(newUniversity))) {
			person.setUniversity(newUniversity);
		}
		// Linkedin
		if (newLinkedIn != null && !("".equals(newLinkedIn))) {
			person.setLinkedin(newLinkedIn);
		}
		dao.updateUserInfo(person);
		return "[\"Updated\"]";
	}

	@Override
	public int createForumPost(String content, String title, Person author, List<ForumCategory> categories) {
		// TODO Auto-generated method stub
		ForumPost post = new ForumPost(author, title, content, GetTimestamp.getCurrentTime(), false);
		post.setCategory(categories);
		return postReplyDao.createForumPost(post);
	}

	@Override
	public List<ForumPost> getAllPosts() {
		return getRidOfDupes(postReplyDao.getAllPosts());
	}

	@Override
	public void createReply(String replyContent, int postId, String username) {
		// TODO Auto-generated method stub
		// ForumPost post = dao.getPostById(postId);
		// System.out.println("postId in the service method: " + post.getId());
		Person author = dao.getPersonByUsername(username);
		postReplyDao.createPostReply(postId, author, new ArrayList<LikeableReply>(), new ArrayList<DisLikeableReply>(), false,
				replyContent, GetTimestamp.getCurrentTime());
	}

	private List<ForumPost> getRidOfDupes(List<ForumPost> posts) {
		List<ForumPost> filteredList = new ArrayList<>();
		for (ForumPost post : posts) {
			if (!filteredList.contains(post))
				filteredList.add(post);
		}
		return filteredList;
	}

	@Override
	@Transactional
	public ForumPost getPostById(int id) {
		return postReplyDao.getPostById(id);
	}

	@Override
	public List<ForumPost> getMorePosts(int firstResult) {
		// TODO Auto-generated method stub
		return getRidOfDupes(postReplyDao.getMorePosts());
	}

	@Transactional
	@Override
	public void addDislike(ForumPost post, Person person) {

		DisLikeablePost dislike = new DisLikeablePost(person, post);

		boolean exist = false;

		for (DisLikeablePost dis : post.getDislikes()) {
			if (dis.getAuthor().getUsername().equals(person.getUsername())) {
				exist = true;
			}
		}

		System.out.println("in add dislike part 2");
		if (exist) {
			System.out.println("in add dislike part 3");
		} else {
			likeDislikeDao.saveDislikePost(dislike);
			post.getDislikes().add(dislike);
			System.out.println(post.getDislikes().size());
			postReplyDao.updatePost(post);
		}

	}

	@Transactional
	@Override
	public void checkForLike(ForumPost post, Person person) {
		// LikeablePost likeable = null;

		System.out.println("in check like");
		// likeable = dao.getLikesByPerson(person);
		for (LikeablePost like : post.getLikes()) {
			if (like.getAuthor().getUsername().equals(person.getUsername())) {
				LikeablePost likeable = likeDislikeDao.getLikesById(like.getId());
				// LikeablePost likes = dao.getLikeById(like.getId());
				// post.getLikes().remove(likeable);
				// dao.updatePost(post);
				likeDislikeDao.removeLikePost(likeable);
				// System.out.println(likeable.toString());
				// dao.removeLike(post, likeable);

			}
		}

	}

	@Transactional
	@Override
	public void checkForDislike(ForumPost post, Person person) {
		// LikeablePost likeable = null;

		System.out.println("in check like");
		// likeable = dao.getLikesByPerson(person);
		for (DisLikeablePost dislike : post.getDislikes()) {
			if (dislike.getAuthor().getUsername().equals(person.getUsername())) {
				DisLikeablePost dislikeable = likeDislikeDao.getDislikesPostById(dislike.getId());
				// LikeablePost likes = dao.getLikeById(like.getId());
				// post.getLikes().remove(likeable);
				// dao.updatePost(post);
				likeDislikeDao.removeDislikePost(dislikeable);
				// System.out.println(likeable.toString());
				// dao.removeLike(post, likeable);

			}
		}

	}

	@Override
	@Transactional
	public List<DisLikeablePost> getAllDislikebyPost(ForumPost post) {

		return post.getDislikes();
	}

	@Override
	@Transactional
	public void addLike(ForumPost post, Person person) {

		LikeablePost like = new LikeablePost(person, post);

		boolean exist = false;

		for (LikeablePost likeable : post.getLikes()) {
			if (likeable.getAuthor().getUsername().equals(person.getUsername())) {
				exist = true;
			}
		}

		if (exist) {

		} else {
			likeDislikeDao.saveLikePost(like);
			post.getLikes().add(like);
			postReplyDao.updatePost(post);
		}
	}

	@Override
	public ForumPost getPostForDislike(int id) {
		return postReplyDao.getPostForDislike(id);
	}

	@Override
	public PostReply getReplyForDislike(int id){
		return postReplyDao.getReplyForDislike(id);
	}
	
	@Override
	public ForumPost getPostForLike(int id) {
		return postReplyDao.getPostForLike(id);
	}

	@Override
	public List<LikeablePost> getAllLikesbyPost(ForumPost post) {
		return post.getLikes();
	}

	@Override
	public List<ForumPost> getPostsByUsername(int firstResult, String username) {
		// TODO Auto-generated method stub
		Person author = dao.getPersonByUsername(username);
		return getRidOfDupes(postReplyDao.getMorePostsByAuthor(author));
	}

	@Override
	public void deletePost(int postId) {
		// TODO Auto-generated method stub
		postReplyDao.deleteForumPost(postId);
	}
	public List<PostReply> getRepliesByPost(ForumPost post) {
		return postReplyDao.getRepliesByPost(post);
	}

	@Override
	public void createForumCategory(String categoryName) {
		// TODO Auto-generated method stub
		dao.createForumCategory(categoryName);
	}

	@Override
	public List<ForumPost> getPostsByCategory(String catName) {
		// TODO Auto-generated method stub
		
		List<ForumPost> posts = getRidOfDupes(postReplyDao.getPostsByCategory());
		ForumCategory category = dao.getCategoryByName(catName);
		List<ForumPost> filteredPosts = new ArrayList<>();
		for(ForumPost post: posts){
			for(ForumCategory cat: post.getCategory()){
				if(cat.getCategoryName().equals(catName)){
					filteredPosts.add(post);
					break;
				}
			}
		}
		return filteredPosts;
	}

	@Override
	public List<ForumCategory> getAllCategories() {
		// TODO Auto-generated method stub
		return getRidOfDupesCategory(dao.getForumCategory());
	}
	
	private List<ForumCategory> getRidOfDupesCategory(List<ForumCategory> categories) {
		List<ForumCategory> filteredList = new ArrayList<>();
		for (ForumCategory cat : categories) {
			if (!filteredList.contains(cat))
				filteredList.add(cat);
		}
		return filteredList;
	}

	@Override
	public ForumCategory getCategoryByName(String catName) {
		// TODO Auto-generated method stub
		return dao.getCategoryByName(catName);
	}
	
	public void addDislikeReply(PostReply reply, Person person) {
		
		DisLikeableReply dislike = new DisLikeableReply(person, reply);

		boolean exist = false;

		for (DisLikeableReply dis : reply.getDislikes()) {
			if (dis.getAuthor().getUsername().equals(person.getUsername())) {
				exist = true;
			}
		}

		System.out.println("in add dislike part 2");
		if (exist) {
			System.out.println("in add dislike part 3");
		} else {
			likeDislikeDao.saveDislikeReply(dislike);
			reply.getDislikes().add(dislike);
			System.out.println(reply.getDislikes().size());
			postReplyDao.updateReply(reply);
		}
		
	}
	
	@Override
	public void addlikeReply(PostReply reply, Person person) {
		
		LikeableReply like = new LikeableReply(person, reply);
		
		boolean exist = false;
		
		for(LikeableReply liken : reply.getLikes()) {
			if (liken.getAuthor().getUsername().equals(person.getUsername())) {
				exist = true;
			}
		}
		
		
		
		System.out.println("in add like part 2");
		if (exist) {
			System.out.println("in add like part 3");
		} else {
			System.out.println("in add like part 4");
			likeDislikeDao.saveLikeReply(like);
			reply.getLikes().add(like);
			postReplyDao.updateReply(reply);
			System.out.println("in add like part 5");

		}
		
	}

	@Override
	public void checkReplyLike(PostReply replyLike, Person person) {
		
		for (LikeableReply like : replyLike.getLikes()) {
			if (like.getAuthor().getUsername().equals(person.getUsername())) {
				LikeableReply likeable = likeDislikeDao.getLikesReplyById(like.getId());
				// LikeablePost likes = dao.getLikeById(like.getId());
				// post.getLikes().remove(likeable);
				// dao.updatePost(post);
				likeDislikeDao.removeLikeReply(likeable);
				// System.out.println(likeable.toString());
				// dao.removeLike(post, likeable);

			}
		}
		
	}
	
	@Override
	public void checkReplyDislike(PostReply disLikeReply, Person person) {
		
		for (DisLikeableReply dislike : disLikeReply.getDislikes()) {
			System.out.println("------------------here-------------1----------");
			if (dislike.getAuthor().getUsername().equals(person.getUsername())) {
				DisLikeableReply likeable = likeDislikeDao.getDislikesReplyById(dislike.getId());
				System.out.println("------------------here------2----------------");
				// LikeablePost likes = dao.getLikeById(like.getId());
				// post.getLikes().remove(likeable);
				// dao.updatePost(post);
				likeDislikeDao.removeDislikeReply(likeable);
				// System.out.println(likeable.toString());
				// dao.removeLike(post, likeable);

			}
		}
		
	}

	@Override
	public PostReply getReplyForLike(int id) {
		return postReplyDao.getReplyForLike(id);
	}

	@Override
	public List<DisLikeableReply> getAllDislikebyReply(PostReply reply) {
		return reply.getDislikes();
	}
	
	@Override
	public List<LikeableReply> getAllLikebyReply(PostReply reply) {
		return reply.getLikes();
	}

	@Override
	public List<ForumPost> getPostsByCategoryProf(String catName, String username) {
		// TODO Auto-generated method stub
		Person author = dao.getPersonByUsername(username);
		List<ForumPost> posts = getRidOfDupes(postReplyDao.getPostsByCategoryProf(author));
		ForumCategory category = dao.getCategoryByName(catName);
		List<ForumPost> filteredPosts = new ArrayList<>();
		for(ForumPost post: posts){
			for(ForumCategory cat: post.getCategory()){
				if(cat.getCategoryName().equals(catName)){
					filteredPosts.add(post);
					break;
				}
			}
		}
		return filteredPosts;
	}
}
