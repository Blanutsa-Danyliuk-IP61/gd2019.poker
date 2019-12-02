import React from 'react';

import Card from '../Card';

import './styles.css';

const Table = ({ cards }) => {
    return (
      <div className='table'>
          {cards.map(card => (
              <Card key={`table${card.displayName}`} width={80} shown card={card} />
          ))}
      </div>
    );
};

export default Table;
