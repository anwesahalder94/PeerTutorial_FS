import React, { useState, useEffect } from 'react';
import { adminService } from '../services/api';

const AdminDashboard = () => {
  const [stats, setStats] = useState({});
  const [users, setUsers] = useState([]);
  const [pendingTutors, setPendingTutors] = useState([]);
  const [payouts, setPayouts] = useState([]);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const statsRes = await adminService.getDashboard();
      setStats(statsRes.data);

      const usersRes = await adminService.getUsers();
      setUsers(usersRes.data);

      const tutorsRes = await adminService.getPendingTutors();
      setPendingTutors(tutorsRes.data);

      const payoutsRes = await adminService.getPayouts();
      setPayouts(payoutsRes.data);
    } catch (err) {
      console.error('Failed to load data', err);
    }
  };

  const handleApprove = async (profileId) => {
    try {
      await adminService.approveTutor(profileId);
      loadData();
    } catch (err) {
      alert('Failed to approve tutor');
    }
  };

  const handleReject = async (profileId) => {
    try {
      await adminService.rejectTutor(profileId);
      loadData();
    } catch (err) {
      alert('Failed to reject tutor');
    }
  };

  return (
    <div className="admin-dashboard">
      <section className="stats">
        <h3>Platform Statistics</h3>
        <div className="stats-grid">
          <div className="stat-card">
            <h4>Total Users</h4>
            <p>{stats.totalUsers}</p>
          </div>
          <div className="stat-card">
            <h4>Total Tutors</h4>
            <p>{stats.totalTutors}</p>
          </div>
          <div className="stat-card">
            <h4>Total Bookings</h4>
            <p>{stats.totalBookings}</p>
          </div>
        </div>
      </section>

      <section>
        <h3>Pending Tutor Approvals</h3>
        {pendingTutors.length === 0 ? (
          <p>No pending approvals.</p>
        ) : (
          <div className="pending-tutors">
            {pendingTutors.map((tutor) => (
              <div key={tutor.id} className="tutor-approval-card">
                <h4>{tutor.user?.firstName} {tutor.user?.lastName}</h4>
                <p>{tutor.bio}</p>
                <p>Subject: {tutor.subject}</p>
                <p>Experience: {tutor.yearsOfExperience} years</p>
                <div className="approval-actions">
                  <button onClick={() => handleApprove(tutor.id)} className="btn-success">Approve</button>
                  <button onClick={() => handleReject(tutor.id)} className="btn-danger">Reject</button>
                </div>
              </div>
            ))}
          </div>
        )}
      </section>

      <section>
        <h3>All Users</h3>
        <table className="users-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Active</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.firstName} {user.lastName}</td>
                <td>{user.email}</td>
                <td>{user.role}</td>
                <td>{user.active ? 'Yes' : 'No'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>
  );
};

export default AdminDashboard;