
import React, { useState, useEffect } from 'react';
import 'quill/dist/quill.snow.css';
import ReactQuill from 'react-quill';
import { Button, Container, Form, Modal } from 'react-bootstrap';
import axios from 'axios';
import { useNavigate , useParams } from 'react-router-dom';

export default function NewPost() {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const userId = localStorage.getItem('userId');
  const token = localStorage.getItem('authToken');
  const [base64Image, setBase64Image] = useState('');
  const [errors, setErrors] = useState({});
  const [showSuccessModal, setShowSuccessModal] = useState(false);


  const modules = {
    toolbar: [
      [{ size: ["small", false, "large", "huge"] }],
      ["bold", "italic", "underline", "strike", "blockquote"],
      [{ list: "ordered" }, { list: "bullet" }],
      ["link", "image"],
      [
        { list: "ordered" },
        { list: "bullet" },
        { indent: "-1" },
        { indent: "+1" },
        { align: [] }
      ],
      [{ "color": ["#000000", "#e60000", "#ff9900", "#ffff00", "#008a00", "#0066cc", "#9933ff", "#ffffff", "#facccc", "#ffebcc", "#ffffcc", "#cce8cc", "#cce0f5", "#ebd6ff", "#bbbbbb", "#f06666", "#ffc266", "#ffff66", "#66b966", "#66a3e0", "#c285ff", "#888888", "#a10000", "#b26b00", "#b2b200", "#006100", "#0047b2", "#6b24b2", "#444444", "#5c0000", "#663d00", "#666600", "#003700", "#002966", "#3d1466", 'custom-color'] }],
    ],
    clipboard: {
      matchVisual: false,
    },
  };

  const formats = [
    "header", "height", "bold", "italic",
    "underline", "strike", "blockquote",
    "list", "color", "bullet", "indent",
    "link", "image", "align", "size",
  ];

  const handleTitleChange = (e) => {
    setTitle(e.target.value);
    if (e.target.value.trim()) {
      setErrors((prev) => ({ ...prev, title: null }));
    }
  };

  const handleCoverImageChange = (e) => {
    const selectedFile = e.target.files[0];

    if (selectedFile) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setBase64Image(reader.result);
        setErrors((prev) => ({ ...prev, imageName: null }));
      };
      reader.readAsDataURL(selectedFile);
    }
  };

  const handleContentChange = (content) => {
    setContent(content);
    if (content.trim()) {
      setErrors((prev) => ({ ...prev, content: null }));
    }
  };

  const validateForm = () => {
    const newErrors = {};
    if (!title.trim()) newErrors.title = 'Title is required.';
    if (!content.trim()) newErrors.content = 'Content cannot be empty.';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async () => {
    if (!validateForm()) return;

    try {
      const formData = {
        title,
        imageName: base64Image,
        content,
      };

      const response = await axios.post(
        `http://localhost:9595/api/posts/user/create`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 201) {
        setShowSuccessModal(true);
        setTitle('');
        setContent('');
        setBase64Image('');
      } else {
        console.error('Failed to submit blog post');
      }
    } catch (error) {
      console.error('Error submitting blog post:', error);
    }
  };

  const handleCloseSuccessModal = () => {
    setShowSuccessModal(false);
    navigate('/mainpage'); // Navigate back to the main page
  };
  return (
    <div>
      <Container className="new-post">
        <h3 style={{ margin: '15px 0px' }}>Create your Blog:</h3>
        <Form>
          <Form.Group controlId="title">
            <Form.Label>Title</Form.Label>
            <Form.Control
              type="text"
              value={title}
              onChange={handleTitleChange}
              placeholder="Enter Title"
            />
            {errors.title && <div className="text-danger">{errors.title}</div>}
          </Form.Group>
          <Form.Group controlId="coverImage" className="mb-3">
            <Form.Label>Add Cover Image</Form.Label>
            <Form.Control
              onChange={handleCoverImageChange}
              type="file"
              accept="image/*"
            />
            {errors.imageName && <div className="text-danger">{errors.imageName}</div>}
          </Form.Group>
        </Form>
        <ReactQuill
          theme="snow"
          modules={modules}
          formats={formats}
          placeholder="Write your content ...."
          value={content}
          onChange={handleContentChange}
          style={{ height: '555px', marginBottom: '50px' }}
        />
        {errors.content && <div className="text-danger">{errors.content}</div>}
        <div className="button-container">
          <Button
            variant="contained"
            onClick={handleSubmit}
            style={{
              display: 'flex',
              justifyContent: 'center',
              textTransform: 'uppercase',
              backgroundColor: '#000',
              color: 'white',
              width: '160px',
              borderRadius: '5px',
              position: 'relative',
              margin: '8px 0px',
              transition: 'background-color 0.3s ease',
            }}
          >
            Submit
          </Button>
        </div>
        <Modal show={showSuccessModal} onHide={handleCloseSuccessModal} centered>
          <Modal.Header closeButton>
            <Modal.Title>Success!</Modal.Title>
          </Modal.Header>
          <Modal.Body>Your post has been added successfully.</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={handleCloseSuccessModal}>
              Close
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </div>
  );
}