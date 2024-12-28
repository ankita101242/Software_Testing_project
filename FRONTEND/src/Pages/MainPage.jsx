import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import NavbarComponent from '../Components/NavbarComponent';
import Sidebar from '../Components/Sidebar';
import Footer from '../Components/Footer';
import NewPost from './NewPost';
import axios from 'axios';
import { Row, Col, Card, Button } from 'react-bootstrap';
import parse from 'html-react-parser';
import './MainPage.css'; 

const MainPage = () => {
  const [selectedOption, setSelectedOption] = useState('');
  const navigate = useNavigate();
  const [posts, setPosts] = useState([]);
  const [bookmarks, setBookmarks] = useState([]);
  const token = localStorage.getItem('authToken');

  if(!token){
    localStorage.removeItem(token);
    navigate('/login');
    
  }

  useEffect(() => {
    if (selectedOption === 'Posts') {
      fetchPosts();
    } else if (selectedOption === 'Bookmarks') {
      fetchBookmarks();
    }
  }, [selectedOption]);

  const fetchPosts = async () => {
    try {
      const response = await axios.get('http://localhost:9595/api/posts/user/all', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setPosts(response.data);
      localStorage.setItem('posts', JSON.stringify(response.data));
    } catch (error) {
      console.error('Error fetching posts:', error);
    }
  };

  const fetchBookmarks = async () => {
    try {
      const response = await axios.get('http://localhost:9595/api/bookmarks', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setBookmarks(response.data);
    } catch (error) {
      console.error('Error fetching bookmarks:', error);
    }
  };

  const handleOptionClick = (option) => {
    if (option === 'New Post') {
      setSelectedOption('New Post');
    } else {
      setSelectedOption(option);
    }
  };

  const handleUpdate = (postId) => {
    navigate(`/edit/${postId}`);
  };

  const handleView = (postId) => {
    navigate(`/view/${postId}`);
  };

  const handleDelete = async (postId) => {
    try {
      await axios.delete(`http://localhost:9595/api/posts/user/delete/${postId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setPosts(posts.filter(post => post.postId !== postId));
    } catch (error) {
      console.error('Error deleting post:', error);
    }
  };

  const truncateContent = (content, wordLimit) => {
    const words = content.split(' ');
    if (words.length > wordLimit) {
      return words.slice(0, wordLimit).join(' ') + '...';
    }
    return content;
  };

  return (
    <div className="main-page">
      <NavbarComponent />
      <div className="flex-container" style={{ height: '73.5vh', display: 'flex' }}>
        <Sidebar handleOptionClick={handleOptionClick} />
        <div className="content-box" style={{ flex: '1', padding: '20px', overflowY: 'auto' }}>
          {selectedOption === 'New Post' && <NewPost />}
          {selectedOption === 'Posts' && (
            <div>
              <h2>Posts</h2>
              <Row xs={1} md={2} lg={4} className="g-4">
                {posts.map((post) => (
                  <Col key={post.postId}>
                    <Card style={{ height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                      {post.imageName && (
                        <Card.Img
                          variant="top"
                          src={`${post.imageName}`}
                          alt="Post Image"
                          style={{ height: '200px', objectFit: 'cover' }}
                        />
                      )}
                      <Card.Body style={{ flex: '1' }}>
                        <Card.Title>{post.title}</Card.Title>
                        <Card.Text>{parse(truncateContent(post.content, 20))}</Card.Text>
                      </Card.Body>
                      <div className="button-container">
                        <Button onClick={() => handleView(post.postId)} className="custom-button">
                          View
                        </Button>
                        <Button onClick={() => handleUpdate(post.postId)} className="custom-button">
                          Edit
                        </Button>
                        <Button onClick={() => handleDelete(post.postId)} className="custom-button" variant="danger">
                          Delete
                        </Button>
                      </div>
                    </Card>
                  </Col>
                ))}
              </Row>
            </div>
          )}
          {selectedOption === 'Bookmarks' && (
            <div>
              <h2>Bookmarks</h2>
              <Row xs={1} md={2} lg={4} className="g-4">
                {bookmarks.map((bookmark) => (
                  <Col key={bookmark.postId}>
                    <Card style={{ height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                      {bookmark.imageName && (
                        <Card.Img
                          variant="top"
                          src={`${bookmark.imageName}`}
                          alt="Bookmark Image"
                          style={{ height: '200px', objectFit: 'cover' }}
                        />
                      )}
                      <Card.Body style={{ flex: '1' }}>
                        <Card.Title>{bookmark.title}</Card.Title>
                        <Card.Text>{parse(truncateContent(bookmark.content, 20))}</Card.Text>
                      </Card.Body>
                      <div className="button-container">
                        <Button onClick={() => handleView(bookmark.postId)} className="custom-button">
                          View
                        </Button>
                        <Button onClick={() => handleUpdate(bookmark.postId)} className="custom-button">
                          Edit
                        </Button>
                      </div>
                    </Card>
                  </Col>
                ))}
              </Row>
            </div>
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default MainPage;