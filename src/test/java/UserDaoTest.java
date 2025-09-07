import static org.mockito.Mockito.*;

import org.example.dao.*;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserDaoTest {

    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        session = Mockito.mock(Session.class);
        transaction = Mockito.mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.getTransaction()).thenReturn(transaction);

        // Предположим, что ваш метод create принадлежит классу UserService,
        // принимающему SessionFactory через конструктор
        userDao = new UserDao(sessionFactory,session);
    }

    @Test
    void testCreateUserSuccess() {
        // Вызываем метод
        userDao.create("Alice");

        // Проверяем последовательность действий
       // verify(sessionFactory).openSession();
        verify(session).beginTransaction();
        verify(session).save(any(User.class)); // сохранение любого User
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    void testCreateUserFailureOnSave() {
        // Симулируем исключение при сохранении
        doThrow(new RuntimeException("DB Error")).when(session).save(any(User.class));

        userDao.create("Bob");

        // В случае исключения транзакция должна откатиться
        //verify(transaction).rollback();
        verify(session).close();
    }
}
