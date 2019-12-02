import React from 'react';
import Card from '../Card';

import './styles.css';

const AI = ({ data, showCards }) => {

  const cardContainer = data.active ? (
    data.hand.map(card => (
      <Card
            key={`${data.id + card.displayName}`}
            width={60}
            shown={showCards}
            card={card}
            location='ai'
      />
    ))
  ) : (
    <p>Folded!</p>
  );

  function styles () {
      return {
          gridArea: data.id,
          maxWidth: data.id === 'ai2' ? '180px' : 'auto'
      }
  }

  return (
    <div className='ai-container' style={styles()}>
        <h4>{`AI Opponent ${data.id.slice(-1)}`}</h4>
        <div className="ai">
            <div className='ai-card-container'>{cardContainer}</div>
        </div>
    </div>
  );
};

export default AI;
