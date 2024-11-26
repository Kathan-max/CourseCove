import React, { useState } from 'react';
import axios from 'axios';

function SearchAutocomplete() {
  const [search, setSearch] = useState('');
  const [suggestions, setSuggestions] = useState([]);

  const handleSearch = async (value) => {
    setSearch(value);
    
    if (value.length > 1) {
      try {
        const response = await axios.get(`http://localhost:8080/autocorrect?query=${value}`);
        setSuggestions(response.data);
      } catch (error) {
        console.error('Error fetching suggestions:', error);
      }
    } else {
      setSuggestions([]);
    }
  };

  const handleSuggestionClick = (suggestion) => {
    setSearch(suggestion);
    setSuggestions([]);
  };

  return (
    <div>
      <input 
        type="text" 
        value={search}
        onChange={(e) => handleSearch(e.target.value)}
        placeholder="Search courses"
      />
      {suggestions.length > 0 && (
        <ul>
          {suggestions.map((suggestion, index) => (
            <li 
              key={index} 
              onClick={() => handleSuggestionClick(suggestion)}
            >
              {suggestion}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default SearchAutocomplete;