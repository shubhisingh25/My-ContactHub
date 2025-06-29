console.log("this is java script file");

const toggleSidebar = () => {
  if ($('.sidebar').is(":visible")) {
    $(".sidebar").css("display", "none");
    $(".content").css("margin-left", "0%");
  } else {
    $(".sidebar").css("display", "block");
    $(".content").css("margin-left", "20%");
  }
};

const search = () => {
  const q = $("#search-input").val().trim();

  if (!q) {
    location.reload();
    return;
  }

  fetch(`/search/${q}`)
    .then(r => r.json())
    .then(d => {
      $("#contacts-table-body").html(
        d.map(c => `
<tr>
  <th>SCM2025${c.cId}</th>
  <td>
    <img src="/img/${c.image}" style="height:40px;width:40px;border-radius:50%;object-fit:cover;margin-right:6px;">
    ${c.name}
  </td>
  <td>
    <a href="/user/${c.cId}/contact">${c.email || 'Not provided'}</a>
  </td>
  <td>
    ${c.phone ? `<a href="tel:${c.phone}">${c.phone}</a>` : 'Not provided'}
  </td>
  <td>
    <button onclick="deleteContact(${c.cId})">Delete</button>
    <form method="post" action="/user/update-contact/${c.cId}" style="display:inline;">
      <button type="submit">Update</button>
    </form>
  </td>
</tr>`
        ).join("")
      );
      $(".pagination").hide();
    });
};


// DOMContentLoaded listener for animations
document.addEventListener('DOMContentLoaded', function () {
  // Add scroll animations
  const elements = document.querySelectorAll('.animate__animated');

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          const animation = entry.target.dataset.animate;
          const delay = entry.target.dataset.animateDelay || '0s';

          entry.target.style.animationDelay = delay;
          entry.target.classList.add(`animate__${animation}`);
          observer.unobserve(entry.target);
        }
      });
    },
    {
      threshold: 0.1,
      rootMargin: '0px 0px -50px 0px',
    }
  );

  elements.forEach((element) => {
    observer.observe(element);
  });
});
