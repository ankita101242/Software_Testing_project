import React, { useState, useEffect } from 'react';
import NavbarComponent from '../Components/NavbarComponent';
import { useNavigate } from 'react-router-dom';
import Carousel from 'react-bootstrap/Carousel';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import slide1 from '../Images/Carousel/1.jpg';
import slide2 from '../Images/Carousel/2.jpg';
import slide3 from '../Images/Carousel/7.jpg';
import slide4 from '../Images/Carousel/6.jpg';
import Footer from '../Components/Footer';
import axios from 'axios';
import parse from 'html-react-parser';
import Cookies from 'js-cookie';

const HomePage = () => {
  const [posts, setPosts] = useState([]);
  const navigate = useNavigate();
  const token = localStorage.getItem('authToken');

  const handleView = (postId) => {
    navigate(`/view/${postId}`);
  };

  const truncateContent = (content, wordLimit) => {
    const words = content.split(' ');
    if (words.length > wordLimit) {
      return words.slice(0, wordLimit).join(' ') + '...';
    }
    return content;
  };

  useEffect(() => {
    if (!token) {
      navigate('/login'); // Redirect to login if no token is found
      return;
    }
    const fetchPosts = async () => {
      try {
        const response = await axios.get('http://localhost:9595/api/posts/all', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setPosts(response.data);
        localStorage.setItem('posts', JSON.stringify(response.data));
        sessionStorage.setItem('posts', JSON.stringify(response.data));
        Cookies.set('posts', JSON.stringify(response.data));
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    fetchPosts();
  }, [token]);

  return (
    <div className='fluid'>
      <NavbarComponent />
      <Container fluid style={{ padding: 0 }}>
      <Carousel style={{ width: "100%", height: "65vh", marginBottom: '12px' }}>
          <Carousel.Item>
            <img src={slide1} alt="img1" style={{ width: "100%", height: "65vh", objectFit: "cover" }} />
          </Carousel.Item>
          <Carousel.Item>
             <img src={slide2} alt="img2" style={{ width: "100%", height: "65vh", objectFit: "cover" }} />
          </Carousel.Item>
          <Carousel.Item>
            <img src={slide3} alt="img3" style={{ width: "100%", height: "65vh", objectFit: "cover" }} />
          </Carousel.Item>
          <Carousel.Item>
            <img src={slide4} alt="img4" style={{ width: "100%", height: "65vh", objectFit: "cover" }} />
          </Carousel.Item>
      </Carousel>

        <h2>ARTICLES</h2>
        <Container style={{ marginTop: '5px', marginBottom: '0px' }}>
          <Row xs={1} md={2} lg={4} className="g-4">
            {posts.map((post) => (
              <Col key={post.postId}>
                <Card style={{ height: '100%', marginBottom: '15px' }}>
                  {post.imageName && (
                    <Card.Img
                      variant="top"
                      src={`${post.imageName}`}
                      alt="Post Image"
                      style={{ height: '200px', objectFit: 'cover' }}
                    />
                  )}
                  <Card.Body>
                    <Card.Title>{post.title}</Card.Title>
                    <Card.Text>{parse(truncateContent(post.content, 20))}</Card.Text>
                  </Card.Body>
                  <div className="button-container">
                    <Button onClick={() => handleView(post.postId)} className="custom-button">
                      View
                    </Button>
                  </div>
                </Card>
              </Col>
            ))}
          </Row>
        </Container>
      </Container>
      <Footer />
    </div>
  );
};

export default HomePage;
