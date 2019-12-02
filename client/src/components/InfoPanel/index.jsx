import React, { useRef, useEffect } from 'react';

import './styles.css';

const InfoPanel = ({ messages }) => {
  let div = useRef(null);

  useEffect(() => {
        div.scrollTop = div.scrollHeight;
  });

  return (
    <div className='info-panel' ref={el => {div = el}}>
        {messages.toArray().map(str => (
            <p key={`infomsg${str.slice(0, 40)}`}>{str}</p>
        ))}
    </div>
  );
};

export default InfoPanel;
