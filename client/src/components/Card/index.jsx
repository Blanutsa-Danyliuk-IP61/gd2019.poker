import React from 'react';
import CardBack from '../../assets/images/cardback.jpg';

import './styles.css'

const Card = ({ shown, card, width }) => {

    function cardStyles() {
        return {
            width: `${width}px`,
            color: card.color,
            height: `${width * 1.4}px`,
            backgroundColor: card.highlight ? 'skyblue' : 'white'
        }
    }

    function cardTitleStyles() {
        return {
            fontSize: `${width * 0.3}px`
        }
    }

    function cardSuitStyles() {
        return {
            fontSize: `${width * 0.4}px`
        }
    }

    return (shown ? (
            <div className='card' style={cardStyles()}>
                <h1 className='card-title' style={cardTitleStyles()}>{card.short}</h1>
                <div className='card-suit' style={cardSuitStyles()}>{card.suitEmoji}</div>
            </div>
        ) : (
            <div className='card' style={cardStyles()}>
                <img src={CardBack} alt='Cardback' />
            </div>
        )
    );
};

export default Card;
