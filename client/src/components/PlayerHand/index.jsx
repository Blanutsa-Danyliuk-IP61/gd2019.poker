import React from 'react';

import Card from '../Card';

import './styles.css';

const PlayerHand = (props) => {

    const { hand } = props;

    return (
        <div className='player-hand'>
            {hand.map(card => (
                <Card
                    key={`${card.short + card.suitEmoji}`}
                    shown
                    card={card}
                />
            ))}
        </div>
    );
};

export default PlayerHand;
