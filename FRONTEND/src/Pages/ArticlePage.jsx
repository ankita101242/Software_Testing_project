import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import axios from 'axios';

const ArticlePage = () => {
  const authToken = localStorage.getItem('authToken');
  const [articles, setArticles] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:9595/api/posts/user/all', {
      headers: {
        Authorization: `Bearer ${authToken}`
      }
    })
      .then(response => {
        setArticles(response.data);
      })
      .catch(error => {
        console.error('Error fetching articles:', error);
      });
  }, [authToken]);

  const pageStyle = {
    display: 'flex',
    flexDirection: 'column',
    minHeight: '100vh'
  };

  const contentStyle = {
    flexGrow: 1,
    marginTop: '60px'
  };

  const blackButtonStyle = {
    backgroundColor: 'black',
    borderColor: 'black',
    color: 'white'
  };

  const blackButtonHoverFocusStyle = {
    backgroundColor: '#333',
    borderColor: '#333',
    color: 'white'
  };

  return (
    <div style={pageStyle}>
      <Container style={contentStyle}>
        <h2 className="mt-5">Articles</h2>
        <Row>
          {articles.map((article, index) => (
            <Col key={index} xs={12} md={6} lg={4} className="mb-4">
              <Card>
                <Card.Body>
                  <Card.Title>{article.title}</Card.Title>
                  <Card.Subtitle className="mb-2 text-muted">By {article.author}</Card.Subtitle>
                  <Card.Text>{article.description}</Card.Text>
                  <Button 
                    href={`/articles/${article.id}`} 
                    style={blackButtonStyle}
                    onMouseOver={e => Object.assign(e.target.style, blackButtonHoverFocusStyle)}
                    onMouseOut={e => Object.assign(e.target.style, blackButtonStyle)}
                    onFocus={e => Object.assign(e.target.style, blackButtonHoverFocusStyle)}
                    onBlur={e => Object.assign(e.target.style, blackButtonStyle)}
                  >
                    Read More
                  </Button>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>
    </div>
  );
};

export default ArticlePage;
