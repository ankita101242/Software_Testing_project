import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Form, Button, Modal } from 'react-bootstrap';
import profile from '../Images/profile.jpg';
import NavbarComponent from '../Components/NavbarComponent';
import Footer from '../Components/Footer';
import axios from 'axios';
import Cookies from 'js-cookie';

export default function ProfilePage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [about, setAbout] = useState('');
  const [showModal, setShowModal] = useState(false);

  // Utility functions for storage
  const saveUserDataInSession = (userData) => {
    sessionStorage.setItem('userData', JSON.stringify(userData));
  };

  const saveUserDataInLocalStorage = (userData) => {
    localStorage.setItem('userData', JSON.stringify(userData));
  };

  const saveUserDataInCookies = (userData) => {
    Cookies.set('userData', JSON.stringify(userData), { expires: 7 }); // Expires in 7 days
  };

  const clearAllStorages = () => {
    sessionStorage.clear();
    localStorage.clear();
    Cookies.remove('userData');
  };

  const getUserDataFromStorage = () => {
    const sessionData = sessionStorage.getItem('userData');
    const localData = localStorage.getItem('userData');
    const cookieData = Cookies.get('userData');

    return (
      sessionData || localData || cookieData
        ? JSON.parse(sessionData || localData || cookieData)
        : null
    );
  };

  // Event Handlers
  const handleNameChange = (e) => setName(e.target.value);
  const handleEmailChange = (e) => setEmail(e.target.value);
  const handleAboutChange = (e) => setAbout(e.target.value);

  const handleUpdateDetails = () => {
    const data = { name, email, about };

    axios.put('http://localhost:9595/api/users/update', data, {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` },
    })
      .then((response) => {
        console.log('Details updated successfully:', response.data);
        saveUserDataInSession(response.data);
        saveUserDataInLocalStorage(response.data);
        saveUserDataInCookies(response.data);
        setShowModal(true);
      })
      .catch((error) => {
        console.error('Error updating details:', error);
      });
  };

  const handleDeleteAccount = () => {
    axios.delete('http://localhost:9595/api/users/delete', {
      headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` },
    })
      .then((response) => {
        console.log('Account deleted successfully:', response.data);
        clearAllStorages();
        window.location.href = '/login';
      })
      .catch((error) => {
        console.error('Error deleting account:', error);
      });
  };

  // Fetch user profile data
  useEffect(() => {
    const savedData = getUserDataFromStorage();

    if (savedData) {
      const { name, email, about } = savedData;
      setName(name);
      setEmail(email);
      setAbout(about);
    } else {
      axios.get('http://localhost:9595/api/users/viewProfile', {
        headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` },
      })
        .then((response) => {
          const userData = response.data;
          setName(userData.name);
          setEmail(userData.email);
          setAbout(userData.about);
          saveUserDataInSession(userData);
          saveUserDataInLocalStorage(userData);
          saveUserDataInCookies(userData);
        })
        .catch((error) => {
          console.error('Error fetching profile data:', error);
        });
    }
  }, []);

  // Component UI
  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <NavbarComponent />
      <div style={{ flex: '1' }}>
        <div className="profile" style={{ marginTop: '100px' }}>
          <Container>
            <Row className="mt-5">
              <Col xs={12} md={4} className="text-center">
                <div style={{ width: '400px', height: '400px', borderRadius: '50%', overflow: 'hidden' }}>
                  <div
                    className="profile-image"
                    style={{
                      width: '100%',
                      height: '100%',
                      backgroundImage: `url(${profile})`,
                      backgroundSize: 'cover',
                      backgroundPosition: 'center',
                    }}
                  ></div>
                </div>
              </Col>
              <Col xs={12} md={8} className="align-self-start">
                <div className="card-body" style={{ position: 'relative', minHeight: '600px' }}>
                  <Form>
                    <Form.Group controlId="formName" className="mb-4">
                      <Form.Label>Name</Form.Label>
                      <Form.Control type="text" value={name} onChange={handleNameChange} />
                    </Form.Group>
                    <Form.Group controlId="formEmail" className="mb-4">
                      <Form.Label>Email</Form.Label>
                      <Form.Control type="email" value={email} onChange={handleEmailChange} />
                    </Form.Group>
                    <Form.Group controlId="formAbout" className="mb-4">
                      <Form.Label>About me</Form.Label>
                      <Form.Control
                        as="textarea"
                        value={about}
                        onChange={handleAboutChange}
                        rows={5}
                        style={{ resize: 'none' }}
                      />
                    </Form.Group>
                    <Button variant="dark" onClick={handleUpdateDetails}>
                      Update Details
                    </Button>
                  </Form>
                  <Button
                    variant="danger"
                    onClick={handleDeleteAccount}
                    style={{ position: 'absolute', bottom: '10px', right: '10px' }}
                  >
                    Delete Account
                  </Button>
                </div>
              </Col>
            </Row>
          </Container>
        </div>
      </div>
      <Footer />

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update Successful</Modal.Title>
        </Modal.Header>
        <Modal.Body>Your profile details have been updated successfully.</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
