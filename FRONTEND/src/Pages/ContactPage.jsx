import React, { useState } from 'react';
import NavbarComponent from '../Components/NavbarComponent';
import Footer from '../Components/Footer';
import { Container } from 'react-bootstrap';

const ContactPage = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Form submitted:', { name, email, message });
    setName('');
    setEmail('');
    setMessage('');
  };

  const pageStyle = {
    display: 'flex',
    flexDirection: 'column',
    minHeight: '100vh',
  };

  const contentStyle = {
    flexGrow: 1,
    padding: '20px',
  };

  return (
    <div style={pageStyle}>
      <NavbarComponent />
      <Container style={contentStyle}>
        <div className="mt-5">
          <h2>Contact Us</h2>
          <div className="row">
            <div className="col-md-6" style={{marginTop:'50px'}}>
              <p>You can contact us through the following methods:</p>
              <p>Email: example@example.com</p>
              <p>Phone: +1234567890</p>
              <p>Address: 123 Main Street, City, Country</p>
            </div>
            <div className="col-md-6">
              <form onSubmit={handleSubmit}>
                <div className="form-group">
                  <label htmlFor="name">Name:</label>
                  <input
                    type="text"
                    className="form-control"
                    id="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="email">Email:</label>
                  <input
                    type="email"
                    className="form-control"
                    id="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="message">Message:</label>
                  <textarea
                    className="form-control"
                    id="message"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    required
                  />
                </div>
                <button type="submit" style={{backgroundColor: 'black', color: 'white', border: 'none', padding: '10px 20px', cursor: 'pointer', marginTop: '10px', borderRadius:'5px'}}>Submit</button>
              </form>
            </div>
          </div>
        </div>
      </Container>
      <Footer />
    </div>
  );
};

export default ContactPage;
