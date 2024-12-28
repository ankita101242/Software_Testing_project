import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import NavbarComponent from '../Components/NavbarComponent';
import Footer from '../Components/Footer';
import parse from 'html-react-parser';
import './BlogPage.css'; 

const PostDetailPage = () => {
  const { postId } = useParams();
  const [post, setPost] = useState(null);
  const token = localStorage.getItem('authToken');
  const [imageName, setImageName] = useState('');

  useEffect(() => {
    fetchPost();
  }, [postId]);

  const fetchPost = async () => {
    try {
      const response = await axios.get(`http://localhost:9595/api/posts/user/${postId}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      setPost(response.data);
      setImageName(response.data.imageName);
    } catch (error) {
      console.error('Error fetching post:', error);
    }
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

  const postImageContainerStyle = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    maxWidth: '100%',
    marginBottom: '20px',
    border: '1px solid #ddd',
    padding: '10px',
    borderRadius: '8px',
  };

  const postImageStyle = {
    maxWidth: '100%',
    maxHeight: '500px',
    objectFit: 'contain',
  };

  return (
    <div style={pageStyle}>
      <NavbarComponent />
      <div style={contentStyle} className="post-container">
        {post ? (
          <div>
            <h2 className="post-title">{post.title}</h2>
            {post.imageName && (
              <div style={postImageContainerStyle}>
                <img src={imageName} alt="Post" style={postImageStyle} />
              </div>
            )}
            <div className="post-content">
              {parse(post.content)}
            </div>
          </div>
        ) : (
          <p>Loading post...</p>
        )}
      </div>
      <Footer />
    </div>
  );
};

export default PostDetailPage;