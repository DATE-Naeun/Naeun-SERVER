package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  
    @PersistenceContext
    private final EntityManager em;


    public void save(User user) {
        em.persist(user);
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public User findUserWithSkinType(Long id) {
        return em.createQuery("select u from User u" +
                " join fetch u.skinType s" +
                " where u.id = :id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public List<User> findByName(String name) {
        return em.createQuery("select u from User u where u.name = :name", User.class)
                .setParameter("name", name)
                .getResultList();
    }

    public void delete(User user) {
        em.remove(user);
    }

    public void updatePreferIngr(User user) {
        em.merge(user);
    }

    public void updateDislikeIngr(User user) {
        em.merge(user);
    }

    /* 변경한 user 를 DB에 반영하는 메서드 */
    public void updateUserCosmetic(User user) {
        em.merge(user);
    }
}
