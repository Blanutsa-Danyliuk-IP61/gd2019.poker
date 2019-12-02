import React from 'react';

import Card from '../Card';

import './styles.css';

const PlayerHand = ({ hand }) => {

  return (
      <div className='player-hand'>
          {hand.map(card => (
              <Card
                  key={`player${card.displayName}`}
                  width={120}
                  shown
                  card={card}
                  location="player"
              />
          ))}
      </div>
  );
};

export default PlayerHand;
