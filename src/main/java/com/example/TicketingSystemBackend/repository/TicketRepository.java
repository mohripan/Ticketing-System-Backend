package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketSeverity;
import com.example.TicketingSystemBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    Optional<Ticket> findByAssignedToAndTicketID(User assignedTo, Integer ticketID);
    List<Ticket> findByAssignedToAndTicketTag_Department_DepartmentID(User assignedTo, Integer departmentID);
    List<Ticket> findAll();

    List<Ticket> findByCustomer_CustomerID(Integer customerID);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.createdDate BETWEEN :startDate AND :endDate")
    Long countTicketsByTimeframe(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE tr.replyDate = (SELECT MIN(tr2.replyDate) FROM TicketReply tr2 WHERE tr2.ticket = t) " +
            "AND t.createdDate BETWEEN :startDate AND :endDate")
    Object[] findResponseTimeMetrics(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.ticketTag.department.departmentID = :departmentID AND t.createdDate BETWEEN :startDate AND :endDate")
    Long countTicketsByDepartmentAndTimeframe(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.assignedTo.userID = :userID AND t.createdDate BETWEEN :startDate AND :endDate")
    Long countTicketsByUserAndTimeframe(Integer userID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "COUNT(tr) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE tr.user.userID = :userID AND " +
            "tr.replyDate = (SELECT MIN(tr2.replyDate) FROM TicketReply tr2 WHERE tr2.ticket = t) AND " +
            "t.createdDate BETWEEN :startDate AND :endDate")
    Object[] findUserResponseTimeMetrics(Integer userID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "COUNT(tr) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE tr.user.userID = :userID AND " +
            "t.createdDate BETWEEN :startDate AND :endDate")
    Object[] countUserResponseTimeMetrics(Integer userID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT tr.user.userID, tr.user.userName, DATE(t.createdDate), " +
            "AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "COUNT(tr) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE tr.user.userID = :userID AND " +
            "t.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY tr.user.userID, DATE(t.createdDate)")
    List<Object[]> findDailyResponseTimeMetricsForUser(Integer userID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT tr.user.userID, tr.user.userName, WEEK(t.createdDate), " +
            "AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "COUNT(tr) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE tr.user.userID = :userID AND " +
            "t.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY tr.user.userID, WEEK(t.createdDate)")
    List<Object[]> findWeeklyResponseTimeMetricsForUser(Integer userID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT tr.user.userID, tr.user.userName, MONTH(t.createdDate), " +
            "AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "COUNT(tr) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE tr.user.userID = :userID AND " +
            "t.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY tr.user.userID, MONTH(t.createdDate)")
    List<Object[]> findMonthlyResponseTimeMetricsForUser(Integer userID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT DATE(t.createdDate), AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "COUNT(tr) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE t.ticketTag.department.departmentID = :departmentID AND tr.replyDate = (SELECT MIN(tr2.replyDate) FROM TicketReply tr2 WHERE tr2.ticket = t) AND t.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(t.createdDate)")
    List<Object[]> averageDailyResponseTimeByDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT tr.user.userID, tr.user.userName, AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "COUNT(tr) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE t.ticketTag.department.departmentID = :departmentID AND " +
            "t.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY tr.user.userID")
    List<Object[]> findResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT tr.user.userID, tr.user.userName, DATE(t.createdDate), " +
            "AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "COUNT(tr) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE t.ticketTag.department.departmentID = :departmentID AND " +
            "t.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY tr.user.userID, DATE(t.createdDate)")
    List<Object[]> findDailyResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT tr.user.userID, tr.user.userName, WEEK(t.createdDate), " +
            "AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "COUNT(tr) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE t.ticketTag.department.departmentID = :departmentID AND " +
            "t.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY tr.user.userID, WEEK(t.createdDate)")
    List<Object[]> findWeeklyResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT tr.user.userID, tr.user.userName, MONTH(t.createdDate), " +
            "AVG(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MIN(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "MAX(TIMESTAMPDIFF(SECOND, t.createdDate, tr.replyDate)), " +
            "COUNT(tr) " +
            "FROM Ticket t " +
            "JOIN t.ticketReplies tr " +
            "WHERE t.ticketTag.department.departmentID = :departmentID AND " +
            "t.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY tr.user.userID, MONTH(t.createdDate)")
    List<Object[]> findMonthlyResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t.ticketSeverity.severityName, COUNT(t) FROM Ticket t WHERE t.ticketTag.department.departmentID = :departmentID AND t.createdDate BETWEEN :startDate AND :endDate GROUP BY t.ticketSeverity.severityName")
    List<Object[]> countTicketsBySeverityAndDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT DATE(t.createdDate), COUNT(t) FROM Ticket t WHERE t.ticketTag.department.departmentID = :departmentID AND t.createdDate BETWEEN :startDate AND :endDate GROUP BY DATE(t.createdDate)")
    List<Object[]> dailyTicketTrafficByDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate);
}
