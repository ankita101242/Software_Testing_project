import React, { useState } from 'react';

export default function ShotsPage() {
  const [isOpen, setIsOpen] = useState(false);

  const toggleCard = () => {
    setIsOpen(!isOpen);
  };

  const Card = ({ title, content }) => (
    <div className="col-md-4 mb-4">
      <div className="card" style={{ height: '100%' }}> {/* Added inline CSS */}
        <div className="card-body">
          <h5 className="card-title">{title}</h5>
          <p className="card-text" style={{ fontSize: '0.9rem' }}>
            {content}
          </p>
          <button className="btn btn-link" onClick={toggleCard}>
            {isOpen ? 'Close' : 'Read More'}
          </button>
          {isOpen && (
            <div>
              <p className="card-text mt-3" style={{ fontSize: '0.9rem' }}>
                More content...
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );

  return (
    <div className="container">
      <div className="row justify-content-center mt-5">
        <Card
          title="Card 1"
          content="Lorem ipsum dolor sit amet, consectetur adipiscing elit."
        />
        <Card
          title="Card 2"
          content="Sed nec magna vitae risus sodales fermentum. Nulla facilisi."
        />
        <Card
          title="Card 3"
          content="Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae."
        />
        <Card
          title="Card 4"
          content="Aliquam vel nibh ac ante consectetur interdum nec non odio. Fusce feugiat augue sit amet semper vestibulum."
        />
        <Card
          title="Card 5"
          content="Morbi luctus vehicula lectus eget consectetur. Fusce vulputate sapien ac consequat malesuada."
        />
        <Card
          title="Card 6"
          content="Nullam pharetra erat eget libero faucibus bibendum. Cras quis purus nec diam auctor hendrerit vel sit amet ipsum."
        />
      </div>
    </div>
  );
}
