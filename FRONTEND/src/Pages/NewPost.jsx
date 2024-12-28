import React, { useState } from 'react';
import 'quill/dist/quill.snow.css';
import ReactQuill from 'react-quill';
import { Button, Container, Form, Modal } from 'react-bootstrap';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function NewPost() {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [base64Image, setBase64Image] = useState('');
  const [errors, setErrors] = useState({});
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const token = localStorage.getItem('authToken');

  if (!token) {
    localStorage.removeItem('authToken');
    navigate('/login');
  }

  const modules = {
    toolbar: [
      [{ size: ['small', false, 'large', 'huge'] }],
      ['bold', 'italic', 'underline', 'strike', 'blockquote'],
      [{ list: 'ordered' }, { list: 'bullet' }],
      ['link', 'image'],
      [
        { list: 'ordered' },
        { list: 'bullet' },
        { indent: '-1' },
        { indent: '+1' },
        { align: [] },
      ],
      [
        {
          color: [
            '#000000',
            '#e60000',
            '#ff9900',
            '#ffff00',
            '#008a00',
            '#0066cc',
            '#9933ff',
            '#ffffff',
          ],
        },
      ],
    ],
    clipboard: {
      matchVisual: false,
    },
  };

  const formats = [
    'header',
    'height',
    'bold',
    'italic',
    'underline',
    'strike',
    'blockquote',
    'list',
    'color',
    'bullet',
    'indent',
    'link',
    'image',
    'align',
    'size',
  ];

  const handleTitleChange = (e) => {
    const value = e.target.value;
    setTitle(value);

    if (value.trim()) {
      setErrors((prev) => ({ ...prev, title: '' }));
    }
  };

  const handleCoverImageChange = (e) => {
    const selectedFile = e.target.files[0];

    if (selectedFile) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setBase64Image(reader.result);
        setErrors((prev) => ({ ...prev, coverImage: '' }));
      };
      reader.readAsDataURL(selectedFile);
    } else {
      setErrors((prev) => ({ ...prev, coverImage: 'Cover image is required.' }));
    }
  };

  const handleContentChange = (content) => {
    setContent(content);

    if (content.trim()) {
      setErrors((prev) => ({ ...prev, content: '' }));
    }
  };

  const validateForm = () => {
    const newErrors = {};
    if (!title.trim()) newErrors.title = 'Title is required.';
    if (!content.trim()) newErrors.content = 'Content is required.';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async () => {
    if (!validateForm()) return;

    try {
      const formData = {
        title: title.trim(),
        imageName: base64Image,
        content: content.trim(),
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
        setErrors({});
      } else {
        console.error('Failed to submit blog post');
      }
    } catch (error) {
      console.error('Error submitting blog post:', error);
    }
  };

  const handleCloseSuccessModal = () => {
    setShowSuccessModal(false);
    navigate('/mainpage');
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
            {errors.title && (
              <Form.Text className="text-danger">{errors.title}</Form.Text>
            )}
          </Form.Group>

          <Form.Group controlId="coverImage" className="mb-3">
            <Form.Label>Add Cover Image</Form.Label>
            <Form.Control
              onChange={handleCoverImageChange}
              type="file"
              accept="image/*"
            />
            {errors.coverImage && (
              <Form.Text className="text-danger">{errors.coverImage}</Form.Text>
            )}
          </Form.Group>
        </Form>

        <ReactQuill
          theme="snow"
          modules={modules}
          formats={formats}
          placeholder="Write your content ...."
          onChange={handleContentChange}
          style={{ height: '300px', marginBottom: '20px' }}
        />
        {errors.content && <p className="text-danger">{errors.content}</p>}

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
